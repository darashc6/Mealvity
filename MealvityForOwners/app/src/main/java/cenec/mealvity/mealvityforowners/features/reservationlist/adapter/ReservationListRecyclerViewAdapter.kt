package cenec.mealvity.mealvityforowners.features.reservationlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.mealvity.mealvityforowners.core.reservation.Reservation
import cenec.mealvity.mealvityforowners.databinding.ItemListReservationBinding

class ReservationListRecyclerViewAdapter(private var reservationList: ArrayList<Reservation>): RecyclerView.Adapter<ReservationListRecyclerViewAdapter.ReservationViewHolder>() {
    private var rvListener: ReservationListRecyclerViewAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val _binding = ItemListReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(_binding)
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(reservationList[position], rvListener)
    }

    fun setReservationListRecyclerViewAdapterListener(newListener: ReservationListRecyclerViewAdapterListener) {
        rvListener = newListener
    }

    fun setReservationList(newReservationList: ArrayList<Reservation>) {
        reservationList = newReservationList
        notifyDataSetChanged()
    }

    class ReservationViewHolder(_binding: ItemListReservationBinding): RecyclerView.ViewHolder(_binding.root) {
        private val binding = _binding
        private var isRejectedLayoutExpanded = false
        private var isRejectedReasonLayoutExpanded = false

        fun bind(reservation: Reservation, listener: ReservationListRecyclerViewAdapterListener?) {
            binding.textViewReferenceNumber.text = "Reference Nº: ${reservation.referenceNumber}"
            binding.textViewDate.text = "Date: ${reservation.date}"
            binding.textViewTime.text = "Time: ${reservation.time}"
            binding.textViewNumberGuests.text = "Nº Guest(s): ${reservation.nGuests}"
            binding.textViewStatus.text = "${reservation.reservationStatus}"
            binding.textViewUserName.text = reservation.user!!.fullName
            binding.textViewUserPhoneNumber.text = if (reservation.user!!.phoneNumber.isNullOrEmpty()) "Not available" else reservation.user!!.phoneNumber
            binding.textViewUserEmail.text = reservation.user!!.email

            checkStatus(reservation)

            binding.buttonAccept.buttonAccept.setOnClickListener {
                listener?.onOptionSelected(layoutPosition, Reservation.ReservationStatus.ACCEPTED, "")
            }

            binding.buttonReject.buttonReject.setOnClickListener {
                isRejectedReasonLayoutExpanded = !isRejectedReasonLayoutExpanded
                expandRejectionMessageLayout()
            }

            binding.buttonConfirmRejection.buttonConfirmRejection.setOnClickListener {
                val textReason = binding.editTextRejectionReason.text.toString()
                listener?.onOptionSelected(layoutPosition, Reservation.ReservationStatus.REJECTED, textReason)
                isRejectedReasonLayoutExpanded = false
                expandRejectionMessageLayout()
            }
        }

        private fun expandRejectionLayout() {
            if (!isRejectedLayoutExpanded) {
                binding.layoutUserDetails.visibility = View.GONE
            } else {
                binding.layoutUserDetails.visibility = View.VISIBLE
            }
        }

        private fun expandRejectionMessageLayout() {
            if (!isRejectedReasonLayoutExpanded) {
                binding.layoutRejectionReason.visibility = View.GONE
            } else {
                binding.layoutRejectionReason.visibility = View.VISIBLE
            }
        }

        private fun checkStatus(reservation: Reservation) {
            when (reservation.reservationStatus) {
                Reservation.ReservationStatus.ACCEPTED -> {
                    binding.layoutUserDetails.visibility = View.GONE
                }
                Reservation.ReservationStatus.REJECTED -> {
                    binding.layoutUserDetails.visibility = View.GONE
                }
                else -> {
                    binding.layoutUserDetails.visibility = View.VISIBLE
                    binding.layoutStatusUpdate.visibility = View.GONE
                }
            }
        }
    }

    interface ReservationListRecyclerViewAdapterListener {
        fun onOptionSelected(position: Int, updatedStatus: Reservation.ReservationStatus, reason: String)
    }
}