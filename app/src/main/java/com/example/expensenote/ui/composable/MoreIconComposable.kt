import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensenote.R
import com.example.expensenote.database.entities.ExpenseItemEntity
import com.example.expensenote.viewmodel.ExpenseItemViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MoreIconPopUpMenu(
    expenseItemEntity: ExpenseItemEntity? = null,
    onDismiss: () -> Unit = {},
    viewModel: ExpenseItemViewModel = hiltViewModel()

) {
    val context = LocalContext.current

    var showDialog by remember {
        mutableStateOf(false)
    }

    Popup(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .width(160.dp)
                .height(100.dp)
                .background(
                    color = colorResource(id = R.color.Deepwhite),
                    RoundedCornerShape(12.dp)
                ),
        ) {
            if (showDialog) {
                if (expenseItemEntity != null) {
                    YourScreenContent2(expenseItemEntity =expenseItemEntity ,onDismiss = { showDialog = false })
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 2.dp)
                        .clickable {
                            showDialog = true

                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.editnote),
                        contentDescription = "edit icon",
                        modifier = Modifier.size(28.dp)
                    )
                    Text(text = "Edit", fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(modifier = Modifier.padding(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 2.dp)
                        .clickable {
                            expenseItemEntity?.let {
                                viewModel.showModalSheet()
                                viewModel.selectedExpenseItem = it
                                onDismiss()
                            }
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.remove),
                        contentDescription = "delete icon",
                        modifier = Modifier.size(25.dp)
                    )
                    Text(
                        text = "Delete",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 11.dp)
                    )
                }

            }
        }

    }
}



