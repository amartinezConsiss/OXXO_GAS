package com.example.oxxogas.ui.paymentmethods.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.oxxogas.R
import com.example.oxxogas.domain.models.PaymentMethodsList
import com.example.oxxogas.domain.models.ShopInformation
import com.example.oxxogas.databinding.FragmentPaymentMethodsBinding
import com.example.oxxogas.ui.home.activities.HomeActivity
import com.example.oxxogas.ui.main.fragments.BaseFragment
import com.example.oxxogas.ui.main.utils.Constants
import com.example.oxxogas.ui.main.utils.setCurrencyFormat
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import com.example.oxxogas.ui.newsale.dialogs.SpinPremiaBottomSheetDialog
import com.example.oxxogas.ui.paymentmethods.adapters.PaymentMethodsAdapter
import com.example.oxxogas.ui.paymentmethods.dialogs.CardPaymentBottomSheetDialog
import com.example.oxxogas.ui.paymentmethods.dialogs.CashPaymentBottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.round
import java.math.BigDecimal
import java.math.RoundingMode

class PaymentMethodsFragment : BaseFragment<FragmentPaymentMethodsBinding>() {

    private var adapter: PaymentMethodsAdapter? = null
    private lateinit var backCallback: OnBackPressedCallback
    private var paymentMethodSelected = 0
    private var isSuccessPayment = false
    private var priceUn = BigDecimal.ZERO
    private var totalAmount = BigDecimal.ZERO
    private var quantity = BigDecimal.ZERO
    private var pumpId = 1
    private var description = "0.0"
    private var hasSpinPremia = false

    private var paymentMethods: List<PaymentMethodsList> =
        listOf(
            PaymentMethodsList(1, "Tarjeta", R.drawable.ic_card, false),
            PaymentMethodsList(2, "Efectivo", R.drawable.ic_cash, false),
            PaymentMethodsList(3, "Pago Mixto", R.drawable.ic_mixed_payment, false)
        )

    override fun initBinding(): FragmentPaymentMethodsBinding =
        FragmentPaymentMethodsBinding.inflate(layoutInflater)

    private var parentActivity: HomeActivity? = null

    override fun initView(view: View, savedState: Bundle?) {
        setUpToolbar()
        loadPumpInformation()
        loadDummyInfo()
        initAdapter()
        showSpinPremiaDialog()
        showPaymentMethodSelected()
    }

    private fun setUpToolbar() {
        parentActivity = activity as? HomeActivity
        parentActivity?.setupToolbar(
            binding.toolbarPaymentMethods,
            getString(R.string.new_sale),
            false
        )

        backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Bloqueado
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, backCallback)
    }

    private fun loadPumpInformation() {
        if (arguments?.containsKey(Constants.PUMP_ID_KEY) == true) {
            pumpId = arguments?.getInt(Constants.PUMP_ID_KEY) ?: 1
            hasSpinPremia = arguments?.getBoolean(Constants.HAS_SPIN_PREMIA) ?: false
            binding.tvPump.text = getString(R.string.pum_id, pumpId.toString())
            if (hasSpinPremia) {
                binding.ivLogoSpin.visibility = View.GONE
                showSpinPremiaSuccessSnackBar()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter() {
        adapter = PaymentMethodsAdapter(paymentMethods) { idPaymentMethod ->
            paymentMethodSelected = idPaymentMethod
            paymentMethods = paymentMethods.map {
                it.copy(isSelected = it.idPaymentMethod == idPaymentMethod)
            }
            adapter?.setPaymentMethods(paymentMethods)
            adapter?.notifyDataSetChanged()
            setAvailableButton()
        }

        binding.rvPaymentMethods.adapter = adapter
    }

    private fun setAvailableButton() {
        binding.btnEndPayment.isEnabled = true
        binding.btnEndPayment.setBackgroundResource(R.drawable.green_btn)
    }

    private fun showPaymentMethodSelected() {
        binding.btnEndPayment.setSafeOnClickListener {
            when (paymentMethodSelected) {
                Constants.CARD_PAYMENT_METHOD -> {
                    cardPaymentMethod()
                }

                Constants.CASH_PAYMENT_METHOD -> {
                    cashPaymentMethod()
                }

                Constants.MIXED_PAYMENT_METHOD -> {
                    Bundle().apply {
                        putString(Constants.SHOP_INFORMATION, getShopInformation())
                        findNavController().navigate(R.id.action_mixed_payment_fragment, this)
                    }
                }
            }

        }
    }

    private fun cardPaymentMethod() {
        val cardPaymentDialog = CardPaymentBottomSheetDialog(isSuccessPayment)
        isSuccessPayment = true
        cardPaymentDialog.onPayCardSuccessCallback = {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1000)
                cardPaymentDialog.dismiss()
                goToResumeFragment()
            }
        }

        cardPaymentDialog.show(
            parentFragmentManager,
            Constants.TAG_CARD_PAYMENT_BOTTOM_SHEET_DIALOG
        )
    }

    private fun cashPaymentMethod() {
        val cashDialog = CashPaymentBottomSheetDialog(totalAmount.toDouble())
        cashDialog.onApplyCashPaymentCallback = {
            viewLifecycleOwner.lifecycleScope.launch {
                parentActivity?.showProgressDialog()
                delay(3000)
                parentActivity?.dismissProgressDialog()
                goToResumeFragment()
            }
        }
        cashDialog.show(parentFragmentManager, Constants.TAG_CASH_BOTTOM_SHEET_DIALOG)
    }

    private fun goToResumeFragment() {
        Bundle().apply {
            putString(Constants.SHOP_INFORMATION, getShopInformation())
            findNavController().navigate(
                R.id.action_resume_ticket_fragment,
                this
            )
        }
    }

    private fun getShopInformation(): String =
        Gson().toJson(
            ShopInformation(
                pump = pumpId,
                methodPayment = paymentMethodSelected,
                typeProduct = description,
                quantity = quantity.toDouble(),
                price = priceUn.toDouble(),
                amount = totalAmount.toDouble(),
                total = totalAmount.toDouble(),
                hasSpinPremia = hasSpinPremia
            )
        )

    private fun showSpinPremiaDialog() {
        val spinPremiaDialog = SpinPremiaBottomSheetDialog()
        binding.ivLogoSpin.setSafeOnClickListener {
            spinPremiaDialog.onContinuePhoneActionClick = {
                showAlert()
                spinPremiaDialog.dismiss()
                showSpinPremiaSuccessSnackBar()
            }

            spinPremiaDialog.onReadCodeActionClick = { codeSpin ->
                showAlert()
                showSpinPremiaSuccessSnackBar()
            }

            spinPremiaDialog.onCancelActionClick = {
                spinPremiaDialog.dismiss()
            }

            spinPremiaDialog.show(
                parentFragmentManager,
                Constants.TAG_SPIN_PREMIA_BOTTOM_SHEET_DIALOG
            )
        }
    }

    private fun showAlert() {
        viewLifecycleOwner.lifecycleScope.launch {
            parentActivity?.showProgressDialog()
            delay(3000)
            parentActivity?.dismissProgressDialog()
        }
    }

    private fun showSpinPremiaSuccessSnackBar() {
        Snackbar
            .make(
                requireView(),
                "Tarjeta Spin Premia asociada correctamente",
                Snackbar.LENGTH_SHORT
            )
            .show()
    }

    private fun loadDummyInfo() {
        val productInformationNumber = (1..3).random()
        val quantityNumber = 1.00 + (50.00 - 1.00) * kotlin.random.Random.nextDouble()

        quantity = BigDecimal.valueOf(quantityNumber)
            .setScale(3, RoundingMode.HALF_UP)

        when (productInformationNumber) {
            1 -> {
                description = "Magna"
                priceUn = BigDecimal(24.63)
               val totalOperation = round(24.63 * quantity.toDouble() * 100) / 100.0
                totalAmount = BigDecimal(totalOperation)

            }

            2 -> {
                description = "Premium"
                priceUn = BigDecimal(25.86)
                val totalOperation = round(25.86 * quantity.toDouble() * 100) / 100.0
                totalAmount = BigDecimal(totalOperation)
            }

            3 -> {
                description = "Diesel"
                priceUn = BigDecimal(23.52)
                val totalOperation = round(23.52 * quantity.toDouble() * 100) / 100.0
                totalAmount = BigDecimal(totalOperation)
            }
        }

        binding.tvDescription.text = description
        binding.tvPrice.text = setCurrencyFormat(priceUn)
        binding.tvTotal.text = setCurrencyFormat(totalAmount)
        binding.tvQuantity.text = "$quantity"
    }

}