import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Mensaje() {
    Text(text = "Aquí se mostrarán las notas creadas")
}

@Composable
fun BotonFlotanteMas(
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = {},
        containerColor = Color.Cyan,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
    ) {
        Icon(Icons.Filled.Add, "Agregar nueva nota")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotasLargeTopBar() {
    LargeTopAppBar(
        title = {
            Text(text = "Notas")
        }
    )
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                Color(0xFFEDEDED),
                RoundedCornerShape(8.dp)
            )
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f))

        Icon(
            Icons.Filled.Search,
            contentDescription = "Buscar",
            modifier = Modifier.padding(end = 8.dp),
            tint = Color.DarkGray
        )
    }
}

@Preview
@Composable
fun Menu() {
    Scaffold(
        topBar = {
            NotasLargeTopBar()
        },
        floatingActionButton = {
            BotonFlotanteMas()
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchBar()

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Mensaje()
                }
            }
        }
    )
}