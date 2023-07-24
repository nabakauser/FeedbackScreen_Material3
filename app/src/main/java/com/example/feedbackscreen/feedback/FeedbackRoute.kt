package com.example.feedbackscreen.feedback

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.feedbackscreen.model.Course
import com.example.feedbackscreen.model.Program

@Composable
fun FeedbackRoute (
    viewModel: FeedbackViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    AddFeedbackScreen(
        feedbackType = uiState.feedbackType,
        feedbackTitle = uiState.feedbackTitle,
        onTitleEntered = {viewModel.onFeedbackTitleEntered(it)},
        feedbackMessage = uiState.feedbackMessage,
        onMessageEntered = {viewModel.onFeedbackMessageEntered(it)},
        onFeedbackTypeSelected = {viewModel.onFeedbackTypeSelected(it)},
        onProgramClicked = {viewModel.onProgramSelected(it)},
        programList = listOf(Program("Prog 1"), Program("Prog 2"), Program("Prog 3")),
        programName = uiState.programTitle,
        onCourseClicked = {viewModel.onCourseSelected(it)},
        courseList = listOf(Course("Course 1"),Course("Course 2"),Course("Course 3"),Course("Course 4")),
        courseName = uiState.courseTitle,
        selectedOption = viewModel.selectedOption,
        onOptionSelected = {viewModel.onRadioOptionClicked(it)},
        image = uiState.image,
        setImage = {viewModel.setImage(it)},
        imageList = uiState.imageList,
        onDeleteClicked = {viewModel.onDeleteClicked(it)},
        onSendButtonClicked = {},
        onBackButtonClicked = {},
    )
}