package com.example.oxxogas.ui.resumeticket.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import buildZplCashSpinPremiaTicket
import buildZplCashTicket
import com.example.oxxogas.R
import com.example.oxxogas.domain.models.CardInformation
import com.example.oxxogas.domain.models.ResumeTicketData
import com.example.oxxogas.domain.models.ShopInformation
import com.example.oxxogas.databinding.FragmentResumeTicketBinding
import com.example.oxxogas.domain.models.response.BaseResponse
import com.example.oxxogas.ui.home.activities.HomeActivity
import com.example.oxxogas.ui.main.fragments.BaseFragment
import com.example.oxxogas.ui.main.utils.Constants
import com.example.oxxogas.ui.main.utils.Constants.TAG_SEND_EMAIL_BOTTOM_SHEET_DIALOG
import com.example.oxxogas.ui.main.utils.ZQ320PrinterManager
import com.example.oxxogas.ui.main.utils.buildZplCardSpinPremiaTicket
import com.example.oxxogas.ui.main.utils.buildZplCardTicket
import com.example.oxxogas.ui.main.utils.buildZplMixedTicket
import com.example.oxxogas.ui.main.utils.generateQr
import com.example.oxxogas.ui.main.utils.setCurrencyFormat
import com.example.oxxogas.ui.main.utils.setSafeOnClickListener
import com.example.oxxogas.ui.resumeticket.MixedCardResumeAdapter
import com.example.oxxogas.ui.resumeticket.dialogs.SendEmailBottomSheetDialog
import com.example.oxxogas.ui.resumeticket.interfaces.PrinterCallback
import com.example.oxxogas.ui.resumeticket.utils.DummyData
import com.example.oxxogas.ui.resumeticket.viewmodels.ResumeViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResumeTicketFragment : BaseFragment<FragmentResumeTicketBinding>() {

    private val viewModel by viewModels<ResumeViewModel>()

    private lateinit var sendEmailBottomSheetDialog: SendEmailBottomSheetDialog
    private lateinit var shopInformationResponse: ShopInformation
    private var parentActivity: HomeActivity? = null


    override fun initBinding(): FragmentResumeTicketBinding =
        FragmentResumeTicketBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            }
        )
    }


    override fun initView(view: View, savedState: Bundle?) {
        sendEmailBottomSheetDialog = SendEmailBottomSheetDialog()
        parentActivity = activity as? HomeActivity
        sendEmail()
        loadTicket()
        initObservers()
        val qrBitmap = generateQr("https://oxxogas.com/ticket/12345")
        binding.ivQr.setImageBitmap(qrBitmap)

        binding.btnFinish.setSafeOnClickListener {
            findNavController().navigate(
                R.id.action_home
            )

        }

        showSuccessSnackBar()
    }

    private fun initObservers() {
        viewModel.sendEmailResult.observe(this) {
            when (it) {
                is BaseResponse.Success -> {
                    parentActivity?.dismissProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        "Se envio el correo con éxito",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is BaseResponse.Loading -> {
                    parentActivity?.showProgressDialog()
                }

                is BaseResponse.Error -> {
                    parentActivity?.dismissProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        "Ocurrio un error al enviar el correo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showSuccessSnackBar() {
        Snackbar
            .make(
                requireView(),
                "Pago registrado correctamente",
                Snackbar.LENGTH_SHORT
            )
            .show()
    }

    private fun loadTicket() {
        if (arguments?.containsKey(Constants.SHOP_INFORMATION) == true) {
            val shopInformation = arguments?.getString(Constants.SHOP_INFORMATION)

            val gson = Gson()
            try {
                shopInformationResponse =
                    gson.fromJson(shopInformation, ShopInformation::class.java)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }

            when (shopInformationResponse.methodPayment) {
                Constants.CARD_PAYMENT_METHOD -> {
                    val resumeTicketData = DummyData().getCardData()
                    resumeTicketData.shopInformation = shopInformationResponse
                    initBtnPrint(resumeTicketData)

                    loadCardInformation(DummyData().getCardData().cardInformation)
                    loadInformation(DummyData().getCardData())
                    binding.tvPaymentMethodTicket.text =
                        getString(R.string.payment_method_ticket, getString(R.string.card))
                    binding.tvPaymentMethodAmount.text =
                        getString(
                            R.string.amount_money,
                            setCurrencyFormat(shopInformationResponse.amount ?: 0.0)
                        )
                }

                Constants.CASH_PAYMENT_METHOD -> {
                    val resumeTicketData = DummyData().getCashData()
                    resumeTicketData.shopInformation = shopInformationResponse
                    initBtnPrint(resumeTicketData)

                    loadInformation(resumeTicketData)
                    binding.cardInformationContainer.visibility = View.GONE
                    binding.tvPaymentMethodTicket.text =
                        getString(R.string.payment_method_ticket, getString(R.string.cash))
                    binding.tvPaymentMethodAmount.text =
                        getString(
                            R.string.amount_money,
                            setCurrencyFormat(shopInformationResponse.amount ?: 0.0)
                        )
                }

                Constants.MIXED_PAYMENT_METHOD -> {
                    val resumeTicketData = DummyData().getMixedData()
                    resumeTicketData.shopInformation = shopInformationResponse
                    initBtnPrint(resumeTicketData)
                    loadInformation(DummyData().getMixedData())
                    loadMixedInformation()
                }
            }
        }
    }

    private fun loadInformation(response: ResumeTicketData) {
        binding.tvNameStation.text = response.station
        binding.addressStationName.text = getString(R.string.address, response.address)
        binding.colabName.text =
            getString(R.string.gas_name_attendant, response.gasStationAttendant)
        binding.tvDateTicket.text = getString(R.string.date, response.date)
        binding.tvTimeTicket.text = getString(R.string.time, response.time)
        binding.tvFolioTicket.text = getString(R.string.folio, response.folio)
        binding.tvPumpTicket.text =
            getString(R.string.pump_resume, shopInformationResponse.pump.toString())
        binding.tvProduct.text = shopInformationResponse.typeProduct
        binding.tvQuantity.text = shopInformationResponse.quantity.toString()
        binding.tvPrice.text = setCurrencyFormat(shopInformationResponse.price ?: 0.0)
        binding.tvAmount.text = setCurrencyFormat(shopInformationResponse.amount ?: 0.0)
        binding.tvTotal.text = setCurrencyFormat(shopInformationResponse.total ?: 0.0)
        if (shopInformationResponse.hasSpinPremia == false) {
            binding.notSpinPremiaContainer.visibility = View.VISIBLE
            binding.hasSpinPremiaContainer.visibility = View.GONE
        } else {
            binding.notSpinPremiaContainer.visibility = View.GONE
            binding.hasSpinPremiaContainer.visibility = View.VISIBLE
            binding.tvSpinPremiaPoints.text =
                getString(
                    R.string.points_spin_premia,
                    String.format("%.2f", DummyData().getPoints())
                )
        }

    }

    private fun loadCardInformation(cardInformation: CardInformation?) {
        val cardInformationBinding = binding.cardPaymentContainer
        binding.cardPaymentContainer.mainCardContainer.visibility = View.VISIBLE
        cardInformationBinding.cardNumber.text = cardInformation?.cardNumber
        cardInformationBinding.bankInformation.text = getString(
            R.string.card_information,
            cardInformation?.bankName,
            cardInformation?.terminalId.toString(),
            cardInformation?.affiliationId.toString()
        )
        cardInformationBinding.tvApproval.text =
            getString(R.string.approval, cardInformation?.approvalId.toString())
        cardInformationBinding.tvArq.text = getString(R.string.arq, cardInformation?.arq)
        cardInformationBinding.tvRef.text = getString(R.string.ref, cardInformation?.ref)
        cardInformationBinding.tvAid.text = getString(R.string.aid, cardInformation?.aid)
        cardInformationBinding.tvAppLabel.text =
            getString(R.string.app_label, cardInformation?.appLabel)
        cardInformationBinding.tvCharge.text = getString(R.string.charge, cardInformation?.charge)
    }

    private fun loadMixedInformation() {
        binding.cardPaymentContainer.mainCardContainer.visibility = View.GONE

        if (shopInformationResponse.cashAmount!! > 0.0) {
            binding.tvPaymentMethodTicket.text =
                getString(R.string.payment_method_ticket, getString(R.string.cash))
            binding.tvPaymentMethodAmount.text =
                getString(
                    R.string.amount_money,
                    setCurrencyFormat(shopInformationResponse.cashAmount ?: 0.0)
                )
        } else {
            binding.tvPaymentMethodTicket.visibility = View.GONE
            binding.tvPaymentMethodAmount.visibility = View.GONE
        }

        if (!shopInformationResponse.cardsMixedInformation.isNullOrEmpty()) {
            val adapter = MixedCardResumeAdapter(
                shopInformationResponse.cardsMixedInformation ?: mutableListOf()
            )
            binding.rvCardsResume.isNestedScrollingEnabled = false
            binding.rvCardsResume.layoutManager =
                LinearLayoutManager(requireContext())
            binding.rvCardsResume.adapter = adapter
            binding.rvCardsResume.setHasFixedSize(false)
        }
    }

    private fun initBtnPrint(resumeTicketData: ResumeTicketData) {
        binding.btnPrint.setSafeOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                requireActivity().runOnUiThread {
                    parentActivity?.showProgressDialog()
                }
                try {
                    val printer = ZQ320PrinterManager("AC:3F:A4:E7:B4:C5", requireContext())
                    var zpl = ""
                    when (resumeTicketData.shopInformation?.methodPayment) {
                        1 -> {
                            zpl = getZplCard(
                                resumeTicketData.shopInformation?.hasSpinPremia == true,
                                resumeTicketData
                            )
                        }

                        2 -> {
                            zpl = getZplCash(
                                resumeTicketData.shopInformation?.hasSpinPremia == true,
                                resumeTicketData
                            )
                        }

                        3 -> {
                            zpl = buildZplMixedTicket(resumeTicketData)
                        }
                    }
                    printer.print(zpl, object : PrinterCallback {
                        override fun onStart() {
                        }

                        override fun onFinish() {
                            requireActivity().runOnUiThread {
                                parentActivity?.dismissProgressDialog()
                                Toast.makeText(
                                    requireContext(),
                                    "Impresión finalizada",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onError(message: String) {
                            requireActivity().runOnUiThread {
                                parentActivity?.dismissProgressDialog()
                                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                            }
                        }

                    })

                } catch (e: Exception) {
                    requireActivity().runOnUiThread {
                        parentActivity?.dismissProgressDialog()
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }

    private fun getZplCard(hasSpin: Boolean, resumeTicketData: ResumeTicketData): String {
        return if (hasSpin) {
            buildZplCardSpinPremiaTicket(
                resumeTicketData
            )
        } else {
            buildZplCardTicket(
                resumeTicketData
            )
        }
    }

    private fun getZplCash(hasSpin: Boolean, resumeTicketData: ResumeTicketData): String {
        return if (hasSpin) {
            buildZplCashSpinPremiaTicket(
                resumeTicketData
            )
        } else {
            buildZplCashTicket(
                resumeTicketData
            )
        }
    }

    private fun sendEmail() {
        binding.btnSendEmail.setSafeOnClickListener {
            sendEmailBottomSheetDialog.show(
                parentFragmentManager,
                TAG_SEND_EMAIL_BOTTOM_SHEET_DIALOG
            )
        }

        sendEmailBottomSheetDialog.onSendEmailClick = { email ->
            viewModel.sendEmail(
                requireContext(),
                email,
                shopInformationResponse.methodPayment ?: 1
            )
        }
    }

}