package cl.nery.examen_ph

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import cl.nery.examen_ph.VM.ClaseVM
import cl.nery.examen_ph.ui.theme.NeryexamenphTheme
import cl.nery.examen_ph.ui.theme.btnColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val ClaseVM by viewModels<ClaseVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeryexamenphTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppCobroJusto(ClaseVM)
                }
            }
        }
    }
}

@Preview
@Composable
fun AppCobroJusto(
    vm: ClaseVM = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") {
            PageInicioUI(
                onClick = {
                    navController.navigate("registro")
                }, vm
            )
        }
        composable("registro") {
            PageFormularioUI(
                onBackClick = { navController.navigate("inicio") }, vm
            )
        }
    }
}

@Composable
fun PageInicioUI(
    onClick: () -> Unit = {},
    vm: ClaseVM = viewModel(),


    ) {
    val contexto = LocalContext.current
    val mainScope = MainScope()

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            vm.mostrarListado(contexto)
        }
    }

    Column {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.logo_justo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = contexto.getString(R.string.titulo_inicio),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        LazyColumn(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            items(vm.itemList) {
                Column() {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        if (it.option == contexto.getString(R.string.Iagua)) {
                            Icon(
                                painter = painterResource(id = R.drawable.agua),
                                contentDescription = "imagenes representativas agua",
                                modifier = Modifier.width(20.dp)
                            )
                        }
                        if (it.option == contexto.getString(R.string.Iluz)) {
                            Icon(
                                painter = painterResource(id = R.drawable.luz),
                                contentDescription = "imagenes representativas luz",
                                modifier = Modifier.width(20.dp)
                            )
                        }
                        if (it.option == contexto.getString(R.string.Igas)) {
                            Icon(
                                painter = painterResource(id = R.drawable.gas),
                                contentDescription = "imagenes representativas gas",
                                modifier = Modifier.width(20.dp)
                            )
                        }

                        Text(it.option.uppercase())
                        Text(it.medidor.toString())
                        Text(it.fecha)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                mainScope.launch { vm.borrarRegistro(contexto, it) }
                            },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.basura),
                                contentDescription = "Borrar registros"
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
        Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
            Button(
                onClick = { onClick() },
                colors = ButtonDefaults.buttonColors(containerColor = btnColor),
                modifier = Modifier
                    .width(160.dp)
                    .height(35.dp)
            ) {
                Text(text = contexto.getString(R.string.nombre_boton))
            }
        }
    }
}

@Preview
@Composable
fun PageFormularioUI(
    onBackClick: () -> Unit = {},
    vm: ClaseVM = viewModel(),
) {
    val contexto = LocalContext.current
    var medidor by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    val options = listOf(
        contexto.getString(R.string.Iagua),
        contexto.getString(R.string.Iluz),
        contexto.getString(R.string.Igas)
    )
    var selectedoption by remember { mutableStateOf("") }
    val mainScope = MainScope()
    val calendar = Calendar.getInstance()
    var datePickerDialogState by remember { mutableStateOf<DatePickerDialog?>(null) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(vertical = 30.dp, horizontal = 30.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_justo),
                contentDescription = "logo",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = contexto.getString(R.string.titulo_pagina_2),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(50.dp))
            TextField(
                value = medidor,
                onValueChange = { medidor = it },
                label = { Text(contexto.getString(R.string.Medidor)) },
                singleLine = true,
                maxLines = 1,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "calendario"
                    )
                }
            )
            TextField(
                value = fecha,
                modifier = Modifier
                    .clickable { datePickerDialogState?.show() },
                onValueChange = { fecha = it },
                label = { Text(text = contexto.getString(R.string.Fecha)) },
                singleLine = true,
                maxLines = 1,
                enabled = false,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "calendario"
                    )
                }
            )
            Spacer(modifier = Modifier.height(60.dp))
            Column(
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = contexto.getString(R.string.MedidorDe),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(horizontal = 60.dp)
                )
                options.forEach { opcion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .selectable(
                                selected = selectedoption == opcion,
                                onClick = { selectedoption = opcion }
                            )
                            .padding(horizontal = 70.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedoption == opcion,
                            onClick = { selectedoption = opcion }
                        )
                        if (opcion == contexto.getString(R.string.Iagua)) {
                            Icon(
                                painter = painterResource(id = R.drawable.agua),
                                contentDescription = "imagenes representativas agua",
                                modifier = Modifier.width(20.dp)
                            )
                        }
                        if (opcion == contexto.getString(R.string.Iluz)) {
                            Icon(
                                painter = painterResource(id = R.drawable.luz),
                                contentDescription = "imagenes representativas luz",
                                modifier = Modifier.width(20.dp)
                            )
                        }
                        if (opcion == contexto.getString(R.string.Igas)) {
                            Icon(
                                painter = painterResource(id = R.drawable.gas),
                                contentDescription = "imagenes representativas gas",
                                modifier = Modifier.width(20.dp)
                            )
                        }
                        Text(text = opcion.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
            Button(
                onClick = {
                    onBackClick(); mainScope.launch {
                    vm.registrarMedicion(medidor, fecha, selectedoption, contexto)
                }
                },
                colors = ButtonDefaults.buttonColors(containerColor = btnColor)
            ) {
                Text(text = contexto.getString(R.string.Registrar_medicion))
            }
        }
    }
    DisposableEffect(contexto) {
        datePickerDialogState = DatePickerDialog(
            contexto,
            { _, year, month, dayOfMonth ->
                fecha = "$dayOfMonth/${month + 1}/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        onDispose {
            datePickerDialogState?.dismiss()
        }
    }
}