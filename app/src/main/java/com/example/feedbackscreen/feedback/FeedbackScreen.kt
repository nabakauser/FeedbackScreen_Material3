package com.example.feedbackscreen.feedback

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.feedbackscreen.R
import com.example.feedbackscreen.model.Course
import com.example.feedbackscreen.model.Program
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun AddFeedbackPreview() {
    AddFeedbackScreen(
        feedbackType = "Course Feedback",
        feedbackTitle = "September course not available",
        onTitleEntered = {},
        feedbackMessage = "Content is not available in your region",
        onMessageEntered = {},
        onFeedbackTypeSelected = {},
        programName = "Program Thirteen",
        onProgramClicked = {},
        programList = listOf(),
        courseName = "Course September",
        onCourseClicked = {},
        courseList = listOf(),
        selectedOption = "Institution",
        onOptionSelected = {},
        image = Uri.EMPTY,
        setImage = {},
        imageList = listOf(),
        onDeleteClicked = {},
        onSendButtonClicked = {},
        onBackButtonClicked = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFeedbackScreen(
    feedbackType: String?,
    feedbackTitle: String?,
    onTitleEntered: (String) -> Unit,
    feedbackMessage: String?,
    onMessageEntered: (String) -> Unit,
    onFeedbackTypeSelected: (String) -> Unit,
    programName: String?,
    onProgramClicked: (String) -> Unit,
    programList: List<Program>,
    courseName: String?,
    onCourseClicked: (String) -> Unit,
    courseList: List<Course>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    image: Uri,
    setImage: (Uri) -> Unit,
    imageList: List<Uri>,
    onDeleteClicked: (Int) -> Unit,
    onSendButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
) {
    val state = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val scope = rememberCoroutineScope()
    val (selectBottomSheet, setSelected) = remember(calculation = { mutableIntStateOf(0) })

    val openBottomSheet = rememberSaveable { mutableStateOf(false) }
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    BottomSheetScaffold(
        scaffoldState = state,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContainerColor = Color.White,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            if (openBottomSheet.value) {
                when (selectBottomSheet) {
                    0 -> {
                        FeedbackTypeBottomSheet(
                            onFeedbackTypeClicked = onFeedbackTypeSelected,
                            modalSheetState = modalSheetState,
                            onDismiss = {
                                scope.launch {
                                    openBottomSheet.value = false
                                    modalSheetState.hide()
                                }
                            }
                        )
                    }

                    1 -> {
                        ProgramFeedbackBottomSheet(
                            onProgramClicked = onProgramClicked,
                            programList = programList,
                            modalSheetState = modalSheetState,
                            onDismiss = {
                                scope.launch {
                                    openBottomSheet.value = false
                                    modalSheetState.hide()
                                }
                            }
                        )
                    }

                    2 -> CourseFeedbackBottomSheet(
                        onCourseClicked = onCourseClicked,
                        courseList = courseList,
                        modalSheetState = modalSheetState,
                        onDismiss = {
                            scope.launch {
                                openBottomSheet.value = false
                                modalSheetState.hide()
                            }
                        }
                    )
                }
            }
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AddFeedBackToolbar(
                    onBackButtonClicked = onBackButtonClicked
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    FeedBackFor(
                        selectedOption = selectedOption,
                        onOptionSelected = onOptionSelected,
                    )
                    if (selectedOption == stringResource(id = R.string.institution)) {
                        FeedbackType(
                            setSelected = setSelected,
                            feedbackType = feedbackType,
                            openDialog = {
                                scope.launch {
                                    openBottomSheet.value = true
                                    modalSheetState.expand()
                                }
                            }
                        )
                        if (feedbackType == stringResource(id = R.string.course_feedback)) {
                            SelectProgram(
                                setSelected = setSelected,
                                programName = programName,
                                openDialog = {
                                    scope.launch {
                                        openBottomSheet.value = true
                                        modalSheetState.expand()
                                    }
                                }
                            )
                            SelectCourse(
                                courseName = courseName,
                                setSelected = setSelected,
                                openDialog = {
                                    scope.launch {
                                        openBottomSheet.value = true
                                        modalSheetState.expand()
                                    }
                                }
                            )
                        } else if (feedbackType == stringResource(id = R.string.program_feedback)) {
                            SelectProgram(
                                setSelected = setSelected,
                                programName = programName,
                                openDialog = {
                                    scope.launch {
                                        openBottomSheet.value = true
                                        modalSheetState.expand()
                                    }
                                }
                            )
                        }
                    }
                    FeedbackTitle(
                        feedbackTitle = feedbackTitle,
                        onTitleEntered = onTitleEntered
                    )
                    FeedbackMessage(
                        feedbackMessage = feedbackMessage,
                        onMessageEntered = onMessageEntered,
                    )
                    AttachFiles(
                        image = image,
                        setImage = setImage,
                        imageList = imageList,
                        onDeleteClicked = onDeleteClicked
                    )
                }
            },
            bottomBar = {
                SendFeedbackButton(
                    onSendButtonClicked = onSendButtonClicked
                )
            }
        )
    }
}


@Composable
fun AddFeedBackToolbar(
    onBackButtonClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .border(border = BorderStroke(width = 1.dp, Color.LightGray))
            .background(Color.White)
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onBackButtonClicked()
        }) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Arrow",
            )
        }
        Text(
            text = stringResource(id = R.string.add_feedback),
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun FeedBackFor(
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        FeedbackTitleText(text = stringResource(id = R.string.feedback_for))
        RadioButtons(
            options = listOf(
                stringResource(id = R.string.institution),
                stringResource(id = R.string.digi_connect),
            ),
            selectedOption = selectedOption ?: "",
            onOptionSelected = onOptionSelected,
        )
    }
}

@Composable
fun FeedbackType(
    feedbackType: String?,
    setSelected: (Int) -> Unit,
    openDialog: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
            .background(Color.White)
    ) {
        FeedbackTitleText(text = stringResource(id = R.string.feedback_type))
        FeedbackDropDown(
            feedbackType = feedbackType,
            placeholderText = stringResource(id = R.string.select_type),
            onDropDownClicked = {
                setSelected(0)
                openDialog()
            }
        )
    }
}


@Composable
fun SelectProgram(
    programName: String?,
    setSelected: (Int) -> Unit,
    openDialog: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .background(Color.White)
    ) {
        FeedbackTitleText(text = stringResource(id = R.string.select_program))
        FeedbackDropDown(
            feedbackType = programName,
            onDropDownClicked = {
                setSelected(1)
                openDialog()
            },
            placeholderText = stringResource(id = R.string.select_program)
        )
    }
}

@Composable
fun SelectCourse(
    courseName: String?,
    setSelected: (Int) -> Unit,
    openDialog: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .background(Color.White)
    ) {
        FeedbackTitleText(text = stringResource(id = R.string.select_course))
        FeedbackDropDown(
            feedbackType = courseName,
            onDropDownClicked = {
                setSelected(2)
                openDialog()
            },
            placeholderText = stringResource(id = R.string.select_course)
        )
    }
}

@Composable
fun FeedbackTitle(
    feedbackTitle: String?,
    onTitleEntered: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .background(Color.White)
    ) {
        FeedbackTitleText(text = stringResource(id = R.string.feedback_title))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            value = feedbackTitle ?: "",
            onValueChange = onTitleEntered,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.type_title),
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        )
    }
}

@Composable
fun FeedbackMessage(
    feedbackMessage: String?,
    onMessageEntered: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .background(Color.White)
    ) {
        FeedbackTitleText(text = stringResource(id = R.string.feedback_message))

        OutlinedTextField(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(top = 5.dp),
            value = feedbackMessage ?: "",
            onValueChange = onMessageEntered,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
            ),
            maxLines = 4,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.type_message),
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        )
    }
}

@Composable
fun AttachFiles(
    image: Uri,
    setImage: (Uri) -> Unit,
    imageList: List<Uri>,
    onDeleteClicked: (Int) -> Unit,
) {
    val context = LocalContext.current
    val launchPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val areGranted = permissions.entries.all { it.value }
        if (areGranted) Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        else Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
    }

    var imageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
                setImage(imageUri)
            }
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .background(Color.White)
    ) {
        val permissions = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
            }
        FeedbackTitleText(text = stringResource(id = R.string.attach_file))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            shape = RoundedCornerShape(3.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            if (image != Uri.EMPTY) {
                imageList.forEachIndexed { index, uri ->
                    Log.d("imageList", "$uri")
                    AttachmentComponents(
                        image = uri,
                        onDeleteClicked = {
                            imageUri = Uri.EMPTY
                            onDeleteClicked(index)
                        },
                        isImage = uri.path?.contains("image") == true
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(.75f),
                    text = stringResource(id = R.string.attachment_types),
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
                Card(
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .clickable {
                            if (permissions.all {
                                    ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                                }) {
                                launcher.launch(arrayOf("image/jpeg", "image/png", "application/pdf"))
                                setImage(imageUri)
                            } else {
                                launchPermission.launch(permissions)
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(15.dp),
                            painter = painterResource(id = R.drawable.ic_attachments),
                            contentDescription = null,
                            tint = colorResource(R.color.digi_blue)
                        )
                        Text(
                            text = stringResource(id = R.string.attach),
                            color = colorResource(R.color.digi_blue),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun SendFeedbackButton(
    onSendButtonClicked: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(2.dp),
        colors = ButtonDefaults.buttonColors(colorResource(R.color.digi_blue)),
        onClick = {
            onSendButtonClicked()
        }) {
        Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send Feedback",
            modifier = Modifier
                .size(25.dp)
                .padding(end = 5.dp),
            tint = Color.White
        )
        Text(
            text = stringResource(id = R.string.send),
            fontSize = 16.sp,
            color = Color.White
        )
    }
}



//bottom sheets
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackTypeBottomSheet(
    onFeedbackTypeClicked: (String) -> Unit,
    onDismiss: (Boolean) -> Unit,
    modalSheetState: SheetState,
) {
    val feedbackList = listOf(
        stringResource(id = R.string.general_feedback),
        stringResource(id = R.string.program_feedback),
        stringResource(id = R.string.course_feedback),
    )
    ModalBottomSheet(
        containerColor = Color.White,
        windowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
        sheetState = modalSheetState,
        onDismissRequest = { onDismiss(false) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(bottom = 56.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = stringResource(id = R.string.feedback_type),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
                feedbackList.forEachIndexed { _, feedbackType ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        Text(
                            text = feedbackType,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .clickable {
                                    onDismiss(false)
                                    onFeedbackTypeClicked(feedbackType)
                                },
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramFeedbackBottomSheet(
    onProgramClicked: (String) -> Unit,
    onDismiss: (Boolean) -> Unit,
    modalSheetState: SheetState,
    programList: List<Program>
) {
    ModalBottomSheet(
        containerColor = Color.White,
        windowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
        sheetState = modalSheetState,
        onDismissRequest = { onDismiss(false) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(bottom = 56.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = stringResource(id = R.string.select_program),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
            programList.forEach { program ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = program.programName ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .clickable {
                                onDismiss(false)
                                onProgramClicked(program.programName ?: "")
                            },
                        fontSize = 16.sp
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseFeedbackBottomSheet(
    onCourseClicked: (String) -> Unit,
    onDismiss: (Boolean) -> Unit,
    modalSheetState: SheetState,
    courseList: List<Course>
) {
    ModalBottomSheet(
        containerColor = Color.White,
        windowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
        sheetState = modalSheetState,
        onDismissRequest = { onDismiss(false) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(bottom = 56.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = stringResource(id = R.string.select_course),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
            courseList.forEach { course ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = course.courseName ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .clickable {
                                onDismiss(false)
                                onCourseClicked(course.courseName ?: "")
                            },
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}




//common composables
@Composable
fun RadioButtons(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .selectableGroup()
            .background(Color.White)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { text ->
            Row(
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable {
                        onOptionSelected(text)
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    modifier = Modifier.offset((-13).dp),
                    onClick = { onOptionSelected(text) },
                    selected = (text == selectedOption),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(R.color.digi_blue),
                        unselectedColor = Color.DarkGray
                    )
                )
                Text(
                    modifier = Modifier.offset((-20).dp),
                    text = text
                )
            }
        }
    }
}

@Composable
fun FeedbackDropDown(
    feedbackType: String?,
    onDropDownClicked: () -> Unit,
    placeholderText: String,
) {
    val focusManager = LocalFocusManager.current
    Card(
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth()
            .clickable {
                onDropDownClicked()
                focusManager.clearFocus()
            },
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(3.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(.9f)
                    .fillMaxWidth()
                    .padding(start = 15.dp),
                text = if(feedbackType.isNullOrBlank()) stringResource(id = R.string.select_type)
                        else feedbackType,
                color = if(feedbackType.isNullOrBlank()) Color.LightGray
                        else Color.Black,
                fontSize = if(feedbackType.isNullOrBlank()) 12.sp
                        else 14.sp
            )
            IconButton(
                onClick = {
                    onDropDownClicked()
                    focusManager.clearFocus()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun FeedbackTitleText(
    text: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Text(
            text = text,
            color = Color.DarkGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun AttachmentComponents(
    image: Uri,
    onDeleteClicked: () -> Unit,
    isImage: Boolean,
) {
    Card(
        shape = RoundedCornerShape(1.dp),
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = if (isImage) Modifier.size(30.dp)
                else Modifier.size(20.dp),
                painter = if (isImage) rememberImagePainter(image)
                else painterResource(id = R.drawable.ic_pdf),
                contentDescription = null
            )
            val documentName = getFileName(LocalContext.current, image)
            Text(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                text = documentName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            IconButton(
                onClick = onDeleteClicked
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "clear upload"
                )
            }
        }
    }
    Divider(thickness = 1.dp, color = Color.LightGray)
}



//util
fun getFileName(
    context: Context,
    uri: Uri
): String {
    val returnCursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor?.moveToFirst()
    val fileName = nameIndex?.let { returnCursor.getString(it).toString() }.toString()
    returnCursor?.close() // cursor must be closed after use
    return fileName
}

