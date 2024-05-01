import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensenote.R
import com.example.expensenote.database.entities.ExpenseItemEntity
import com.example.expensenote.ui.theme.appColor
import com.example.expensenote.viewmodel.ExpenseItemViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseDialog(

    onDismiss: () -> Unit,
    viewModel: ExpenseItemViewModel = hiltViewModel(),
    initialExpenseName: String = "",
    initialExpenseAmount: String = "",
    initialExpenseDescription: String = ""
) {
    var expenseName by remember { mutableStateOf(initialExpenseName) }
    var expenseAmount by remember { mutableStateOf(initialExpenseAmount) }
    var expenseDescription by remember { mutableStateOf(initialExpenseDescription) }
    val singleLine = false
    val time = time1()

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .wrapContentSize()
                .background(Color.White, RoundedCornerShape(12.dp)),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "logo",
                    modifier = Modifier.height(100.dp),
                    contentScale = ContentScale.Crop
                )
                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (expenseName.isNotBlank()) appColor else Color.Red,
                        unfocusedBorderColor = if (expenseName.isNotBlank()) appColor else Color.Red,
                        focusedLabelColor = if (expenseName.isNotBlank()) appColor else Color.Red,
                        cursorColor = if (expenseName.isNotBlank()) appColor else Color.Red,
                    ),
                    value = expenseName,
                    onValueChange = { expenseName = it },
                    label = { Text("Expense Name") },
                    placeholder = { Text("Write your Expense Name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.cost),
                            contentDescription = "null",
                            tint = appColor
                        )
                    },
                    shape = RoundedCornerShape(8.dp)

                )


                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (expenseAmount.isNotBlank()) appColor else Color.Red,
                        unfocusedBorderColor = if (expenseAmount.isNotBlank()) appColor else Color.Red,
                        focusedLabelColor = if (expenseAmount.isNotBlank()) appColor else Color.Red,
                        cursorColor = if (expenseAmount.isNotBlank()) appColor else Color.Red,
                    ),
                    value = expenseAmount,
                    onValueChange = { expenseAmount = it },
                    label = { Text("Expense Amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    placeholder = { Text("Write your Expense Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.money),
                            contentDescription = "null",
                            tint = appColor
                        )
                    },
                    shape = RoundedCornerShape(8.dp)
                )


                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = appColor,
                        focusedLabelColor = appColor,
                        cursorColor = appColor
                    ),
                    value = expenseDescription,
                    onValueChange = { expenseDescription = it },
                    label = { Text("Expense Description") },
                    placeholder = { Text("Write your Expense Description..") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = if (singleLine) 1 else 3,
                    minLines = 1,
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.contentdescription),
                            contentDescription = "null",
                            tint = appColor
                        )
                    },
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Cancel")
                    }

                    Button(
                        onClick = {
                            if (expenseName.isNotBlank() && expenseAmount.isNotBlank()) {
                                viewModel.addExpenseItem(
                                    ExpenseItemEntity(
                                        0,
                                        expenseName,
                                        expenseAmount,
                                        time,
                                        expenseDescription
                                    )
                                )
                                onDismiss()
                                viewModel.hideLottie()

                            } else {
                                Toast.makeText(
                                    context,
                                    "Please fill up require fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = appColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun ShowDiaolog() {
//    ExpenseDialog {
//
//    }
//}


private fun time(): String {

    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

    val currentDateAndTime = sdf.format(Date())

    return currentDateAndTime

}

@RequiresApi(Build.VERSION_CODES.O)
private fun time1(): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        .withLocale(Locale.US)

    // Create a ZonedDateTime with the desired time zone
    val zonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())

    return formatter.format(zonedDateTime)
}