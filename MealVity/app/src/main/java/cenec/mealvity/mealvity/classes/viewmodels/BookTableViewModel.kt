package cenec.mealvity.mealvity.classes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookTableViewModel: ViewModel() {
    private val _reservationDate = MutableLiveData<String>()
    val reservationDate: String get() = _reservationDate.value!!
    private val _reservationTime = MutableLiveData<String>()
    val reservationTime: String get() = _reservationTime.value!!
    private val _nGuests = MutableLiveData<Int>()
    val nGuest: Int get() = _nGuests.value!!

    init {
        _reservationDate.value = ""
        _reservationTime.value = ""
        _nGuests.value = 1
    }

    fun setReservationDate(reservedDate: String) {
        _reservationDate.value = reservedDate
    }

    fun setReservationTime(reservedTime: String) {
        _reservationTime.value = reservedTime
    }

    fun addGuest() {
        _nGuests.value = _nGuests.value!! + 1
    }

    fun removeGuest() {
        val oldNGuests = _nGuests.value!!
        if (oldNGuests > 1) {
            _nGuests.value = oldNGuests - 1
        }
    }

    fun verifyReservation(): Boolean {
        return !(reservationDate.isEmpty() || reservationTime.isEmpty())
    }
}