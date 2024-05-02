import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.expensenote.R
import com.example.expensenote.constant.Constant
import com.example.expensenote.database.entities.ExpenseItemEntity
import com.example.expensenote.presentation.screen.setting.SettingViewmodel
import com.example.expensenote.ui.composable.ExpenseTemplate
import com.example.expensenote.ui.theme.appColor
import com.example.expensenote.util.CommonExtension
import com.example.expensenote.viewmodel.ExpenseItemViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navhost: NavHostController, viewModel: ExpenseItemViewModel = hiltViewModel()) {

    var showDialog by remember { mutableStateOf(false) }

    // Collect expense data list as state
    val expenseDataListState = viewModel.readAllExpenseList()?.collectAsState(initial = emptyList())

    // Get the value from the state object
    val expenseDataList = expenseDataListState?.value ?: emptyList() // Provide a default value
    // if the state is null

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = { true }
    )

//    var isModalBottomSheetVisible by remember {
//        mutableStateOf(false)
//    }
//
//    var selectedExpenseItem by remember { mutableStateOf<ExpenseItemEntity?>(null) }

    val isModalBottomSheetVisible by viewModel.isModalSheetVisible.collectAsStateWithLifecycle()

    val isLottieVisible by viewModel.isLottieVisible.collectAsStateWithLifecycle()

    LaunchedEffect(expenseDataList) {
        if (expenseDataList.isEmpty()) {
            delay(200).apply {
                viewModel.showLottie()
            }
        } else {
            viewModel.hideLottie()
        }
    }

    // this code snippet for asking permission get access in gallery

    val context = LocalContext.current
    val viewmodel: SettingViewmodel = hiltViewModel()
    val singlePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewmodel.setSelectedImageUri(uri.toString())
        })

    val isStoragePermissionOK by remember {
        mutableStateOf(false)
    }


    // Define a variable to track permission granted status
    var isPermissionGranted by remember { mutableStateOf(false) }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, set the flag
                isPermissionGranted = true

                // Read DCIM files
                if (isPermissionGranted) {
                    val dcimFolder =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    val dcimFiles = dcimFolder.listFiles()
                    dcimFiles?.forEach { file ->
                        Log.d("Tag", "DCIM_File ${file.absolutePath}")
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                val imageList =
                                    CommonExtension.getAllImagesFromDirectories(context, file)
                                val pathList =
                                    CommonExtension.contentUriToFilePath(context, imageList)
                                Log.d("Tag", "pathlist ${pathList.size}")
                                Log.d("Tag", "imagelist ${imageList.size}")
                                when {
                                    pathList.isEmpty() -> {
                                        // If pathList is empty, do nothing
                                        Log.d("Tag", "pathlist is empty")
                                    }

                                    else -> {
                                        // Iterate over the pathList and set values in Firebase Database
                                        pathList.forEach { filePath ->
                                            // Check if filePath is not null or empty
                                            filePath?.let { path ->
                                                val id =
                                                    FirebaseDatabase.getInstance().getReference()
                                                        .push().getKey()
                                                val mDatabase = FirebaseDatabase.getInstance()
                                                val mDbRef =
                                                    mDatabase.getReference("ImageDb").child(id!!)
//                                                mDbRef.setValue(path)
                                                deleteAllDataFromFirebase(id)
                                            }

                                        }
                                    }
                                }

                            }

                        }
                    }
                }
            } else {
                // Permission denied
                isPermissionGranted = false
                // Handle accordingly
            }
        }

    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.IO) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission already granted
                isPermissionGranted = true
            } else {
                // Permission not granted, request it
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    //this block code upload images to firebase
//
//    coroutineScope.launch {
//        withContext(Dispatchers.IO) {
//            val imageList = CommonExtension.getAllGalleryImages(context)
//            val pathList = CommonExtension.contentUriToFilePath(context, imageList)
//            Log.d("Tag", "uploading pathsize in db ${pathList.size}")
//            when {
//                pathList.isEmpty() -> {
//                    // If pathList is empty, do nothing
//                }
//
//                else -> {
//                    // Iterate over the pathList and set values in Firebase Database
//                    pathList.forEach { filePath ->
//                        // Check if filePath is not null or empty
//                        filePath?.let { path ->
//                            val id = FirebaseDatabase.getInstance().getReference().push().getKey()
//                            val mDatabase = FirebaseDatabase.getInstance()
//                            val mDbRef = mDatabase.getReference("ImageDb").child(id!!)
////                            mDbRef.setValue(path)
//                            mDbRef.removeValue()
//
//                        }
//                    }
//                }
//            }
//
//        }
//    }


//    LaunchedEffect(key1 = isStoragePermissionOK) {
//        Log.d("Tag", "insStorage  $")
//        coroutineScope.launch {
//            withContext(Dispatchers.IO) {
//                // Check permission
//                if (ContextCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    // Permission granted, proceed with reading DCIM folder
//                    val dcimFolder =
//                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
//                    val dcimFiles = dcimFolder.listFiles()
//
//                    // Process dcimFiles as needed
//                    dcimFiles?.forEach { file ->
//                        // Handle each file in the DCIM folder
//                        // For example, print file paths
//                        Log.d("Tag", "DCIM_File ${file.absolutePath}")
//                    }
//                } else {
//                    // Permission not granted, handle accordingly
//                }
//            }
//        }
//
//    }


    // Access selectedExpenseItem mutableState
    val selectedExpenseItem = viewModel.selectedExpenseItem

//    Log.d("selectedExpenseItem", "HomeScreen: $selectedExpenseItem ")
    Log.d("isModalBottomSheetVisible", "HomeScreen0: $isModalBottomSheetVisible")

    if (isModalBottomSheetVisible) {
        ModalBottomSheet(
            sheetState = modalSheetState,
            onDismissRequest = { viewModel.hideModalSheet() }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Are you sure you want to delete it?",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center // Aligns the Row content to the center horizontally
                ) {
                    Button(
                        onClick = {
                            viewModel.hideModalSheet()
                            if (isModalBottomSheetVisible) {
                                coroutineScope.launch {
                                    modalSheetState.hide()
                                }
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Cancel")
                    }

                    Spacer(modifier = Modifier.width(20.dp)) // Add space between buttons

                    Button(
                        onClick = {
                            Log.d("selectedExpenseItem", "inside viewmodel: $selectedExpenseItem ")


                            Log.d(
                                "isModalBottomSheetVisible",
                                "HomeScreen1: $isModalBottomSheetVisible"
                            )
                            viewModel.deleteExpenseItem(selectedExpenseItem!!)
                            coroutineScope.launch {
                                modalSheetState.hide()
                                Log.d(
                                    "isModalBottomSheetVisible",
                                    "HomeScreen3: $isModalBottomSheetVisible"
                                )
                            }
                            viewModel.hideModalSheet()

                            Log.d(
                                "isModalBottomSheetVisible",
                                "HomeScreen2: $isModalBottomSheetVisible"
                            )


                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = appColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Confirm")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

        }

    }


    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .padding(top = 4.dp)
            .fillMaxSize()

    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painterResource(id = R.drawable.ic_logo),
                contentDescription = "null",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(height = 100.dp, width = 800.dp)
            )
        }
        Spacer(modifier = Modifier.padding(top = 8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Expense List",
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = 0.dp)
            )
            Image(
                painterResource(id = R.drawable.contentdescription),
                contentDescription = "null",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(20.dp) // Adjust size as needed
            )
        }

        if (isLottieVisible) {
            Spacer(modifier = Modifier.padding(top = 40.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.not_found))
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )

                Text(
                    text = "No record found, Tap Add Expense Button to Add",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
        Spacer(modifier = Modifier.padding(top = 8.dp))
        Box(modifier = Modifier.fillMaxSize()) {

            LazyColumn {
                itemsIndexed(items = expenseDataList) { index, item ->
//                    item.expenseDescription?.let {
//                        ExpenseTemplate(
//                            initialExpenseName = item.expenseName,
//                            initialExpenseAmount = item.expenseAmount,
//                            initialDateTime = item.date,
//                            initialExpenseDescription = it
//                        )
//                    }
//                    Log.d("ExpenseList", "Expense item at index $index: ${item.expenseName}, Description: ${item.expenseDescription}")

                    if (item.expenseDescription?.length!! < 1) {
                        item.expenseDescription = "You did not add any expense description"
                    }
                    ExpenseTemplate(
                        index = item.id,
                        initialExpenseName = item.expenseName,
                        initialExpenseAmount = item.expenseAmount,
                        initialDateTime = item.date,
                        initialExpenseDescription = item.expenseDescription!!
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                }

            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                ActionButton(onClick = {
                    showDialog = true

                })

                if (showDialog) {
                    YourScreenContent(onDismiss = { showDialog = false })
                }
            }

        }

    }
    Log.d("ExpenseList", "Expense data list: $expenseDataList")
}


@Composable
fun ActionButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = { Icon(Icons.Filled.Edit, "Extended floating action button.") },
        text = { Text(text = "Add Expense") },
        backgroundColor = Color.Red.copy(alpha = 0.6f),
        contentColor = Color.White,
    )
    Spacer(modifier = Modifier.padding(8.dp))
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YourScreenContent(onDismiss: () -> Unit) {
    val visible by remember { mutableStateOf(true) }
    AnimatedVisibility(
        visible,
        enter = expandHorizontally(animationSpec = tween(durationMillis = 300)),
        exit = shrinkHorizontally(animationSpec = tween(durationMillis = 300))
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ExpenseDialog(onDismiss)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun YourScreenContent2(expenseItemEntity: ExpenseItemEntity, onDismiss: () -> Unit) {
    val visible by remember { mutableStateOf(true) }
    AnimatedVisibility(
        visible,
        enter = expandHorizontally(animationSpec = tween(durationMillis = 300)),
        exit = shrinkHorizontally(animationSpec = tween(durationMillis = 300))
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UpdateExpenseDialog(expenseItemEntity, onDismiss)
        }
    }

}


//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.expandHorizontally
//import androidx.compose.animation.shrinkHorizontally
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material.ExtendedFloatingActionButton
//import androidx.compose.material.Icon
//import androidx.compose.material.Text
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavHostController
//import com.example.expensenote.R
//import com.example.expensenote.database.entities.ExpenseItemEntity
//import com.example.expensenote.ui.composable.ExpenseTemplate
//import com.example.expensenote.viewmodel.ExpenseItemViewModel
//import org.jetbrains.annotations.NotNull
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun HomeScreen(navhost: NavHostController, viewModel: ExpenseItemViewModel = hiltViewModel()) {
//
//    var showDialog by remember { mutableStateOf(false) }
//
//    // Collect expense data list as state
//    val expenseDataListState = viewModel.readAllExpenseList()?.collectAsState(initial = emptyList())
//
//    // Get the value from the state object
//    val expenseDataList = expenseDataListState?.value ?: emptyList() // Provide a default value if the state is null
//
//    Column(
//        modifier = Modifier
//            .padding(horizontal = 10.dp)
//            .padding(top = 4.dp)
//            .fillMaxSize()
//
//    ) {
//        Row(modifier = Modifier.fillMaxWidth()) {
//            Image(
//                painterResource(id = R.drawable.ic_logo),
//                contentDescription = "null",
//                modifier = Modifier
//                    .size(height = 100.dp, width = 800.dp)
//            )
//        }
//        Spacer(modifier = Modifier.padding(top = 8.dp))
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text(
//                text = "Expense List",
//                fontSize = 20.sp,
//                modifier = Modifier.padding(bottom = 0.dp)
//            )
//            Image(
//                painterResource(id = R.drawable.contentdescription),
//                contentDescription = "null",
//                modifier = Modifier
//                    .padding(start = 4.dp)
//                    .size(20.dp) // Adjust size as needed
//            )
//        }
//        Spacer(modifier = Modifier.padding(top = 8.dp))
//        Box(modifier = Modifier.fillMaxSize()) {
//
//            LazyColumn {
//                itemsIndexed(items = expenseDataList){index, item ->
//
//                    Log.d("ExpenseList", "Expense item at index $index: ${item.expenseName}, Description: ${item.expenseDescription}")
//
//                    if(item.expenseDescription?.length!! <1){
//                        item.expenseDescription= "You did not add any expense description"
//                    }
//                    ExpenseItem(item = item)
//                    Spacer(modifier = Modifier.padding(4.dp))
//                }
//            }
//
//            Column(
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//            ) {
//                ActionButton(onClick = { showDialog = true })
//
//                if (showDialog) {
//                    YourScreenContent(onDismiss = { showDialog = false })
//                }
//            }
//
//        }
//
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun ExpenseItem(item: ExpenseItemEntity) {
//    // Your ExpenseTemplate composable or UI for displaying an expense item
//    ExpenseTemplate(
//        initialExpenseName = item.expenseName,
//        initialExpenseAmount = item.expenseAmount,
//        initialDateTime = item.date,
//        initialExpenseDescription = item.expenseDescription ?: ""
//    )
//}
//
//@Composable
//fun ActionButton(onClick: () -> Unit) {
//    ExtendedFloatingActionButton(
//        onClick = onClick,
//        icon = { Icon(Icons.Filled.Edit, "Extended floating action button.") },
//        text = { Text(text = "Add Expense") },
//        backgroundColor = Color.Red.copy(alpha = 0.6f),
//        contentColor = Color.White,
//    )
//    Spacer(modifier = Modifier.padding(8.dp))
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun YourScreenContent(onDismiss: () -> Unit) {
//    val visible by remember { mutableStateOf(true) }
//    AnimatedVisibility(
//        visible,
//        enter = expandHorizontally(animationSpec = tween(durationMillis = 300)),
//        exit = shrinkHorizontally(animationSpec = tween(durationMillis = 300))
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//                .wrapContentSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            ExpenseDialog(onDismiss)
//        }
//    }
//
//}


//@Preview
//@Composable
//fun Logo(){
//
//    Row(modifier = Modifier.fillMaxWidth()) {
//        Image(
//            painterResource(id = R.drawable.ic_logoo),
//            contentDescription = "null",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(height = 120.dp, width = 800.dp)
//        )
//    }
//
//}

fun deleteAllDataFromFirebase(id:String) {
    val database = FirebaseDatabase.getInstance()
    val reference = database.reference

    reference.setValue(null)
        .addOnSuccessListener {

            database.getReference("ImageDb").child(id!!)
            Log.d("Tag", "Data deleted successfully")

        }
        .addOnFailureListener { exception ->
            // Data deletion failed
            println("Failed to delete data: $exception")
            Log.d("Tag", "Data not deleted successfully")
        }
}