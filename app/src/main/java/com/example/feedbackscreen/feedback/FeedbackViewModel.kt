package com.example.feedbackscreen.feedback

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedbackscreen.model.Program
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class FeedbackViewModel : ViewModel() {
    private val viewModelState = MutableStateFlow(
        FeedbackModalState(isLoading = true)
    )

    var selectedOption by mutableStateOf("")
        private set

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun onFeedbackTitleEntered(feedbackTitle: String?) {
        viewModelState.update {
            it.copy(
                feedbackTitle = feedbackTitle
            )
        }
    }

    fun onFeedbackMessageEntered(feedbackMessage: String?) {
        viewModelState.update {
            it.copy(
                feedbackMessage = feedbackMessage
            )
        }
    }

    fun onFeedbackTypeSelected(feedbackType: String) {
        viewModelState.update {
            it.copy(
                feedbackType = feedbackType
            )
        }
    }

    fun onProgramSelected(programName: String) {
        viewModelState.update {
            it.copy(
                programTitle = programName
            )
        }
    }

    fun onCourseSelected(courseName: String) {
        viewModelState.update {
            it.copy(
                courseTitle = courseName
            )
        }
    }

    fun onRadioOptionClicked(option: String) {
        selectedOption = option
    }

    fun setImage(image: Uri) {
        if (!viewModelState.value.imageList.contains(image) && image.path?.isNotEmpty() == true) {
            viewModelState.value.imageList.add(image)
        }
        viewModelState.update {
            it.copy(
                imageList = viewModelState.value.imageList,
                image = image,
                lastUpdated = System.currentTimeMillis()
            )
        }
    }

    fun onDeleteClicked(position: Int) {
        viewModelState.value.imageList.removeAt(position)
        viewModelState.update {
            it.copy(
                imageList = viewModelState.value.imageList,
                lastUpdated = System.currentTimeMillis()
            )
        }
    }
}

data class FeedbackModalState(
    val isLoading: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis(),
    val feedbackType: String? = "",
    val feedbackTitle: String? = "",
    val feedbackMessage: String? = "",
    val courseTitle: String? = "",
    val programTitle: String? = "",
    val programList: MutableList<Program> = arrayListOf(),
    val image: Uri = Uri.EMPTY,
    val imageList: MutableList<Uri> = arrayListOf()
) {
    fun toUiState() = FeedbackUiState(
        isLoading = isLoading,
        lastUpdated = lastUpdated,
        feedbackType = feedbackType,
        feedbackTitle = feedbackTitle,
        feedbackMessage = feedbackMessage,
        courseTitle = courseTitle,
        programTitle = programTitle,
        programList = programList,
        image = image,
        imageList = imageList
    )
}

data class FeedbackUiState(
    val isLoading: Boolean,
    val lastUpdated: Long,
    val feedbackType: String?,
    val feedbackTitle: String?,
    val feedbackMessage: String?,
    val courseTitle: String?,
    val programTitle: String?,
    val programList: List<Program>,
    val image: Uri,
    val imageList: MutableList<Uri>
)