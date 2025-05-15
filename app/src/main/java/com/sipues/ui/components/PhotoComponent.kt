package com.sipues.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.sipues.R
import java.io.File

@Composable
fun PhotoComponent(
    photoPath: String?,
    onPhotoTaken: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val showPermissionDialog = remember { mutableStateOf(false) }
    val showRationaleDialog = remember { mutableStateOf(false) }

    // Crear un archivo temporal para la imagen capturada
    val file = remember {
        File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_${System.currentTimeMillis()}.jpg")
    }

    // Generar el Uri usando FileProvider
    val imageUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onPhotoTaken(file.absolutePath)
            }
        }
    )


    // Lanzador para verificar permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permiso concedido, lanzar la cámara
            imageUri?.let { uri ->
                cameraLauncher.launch(uri)
            }
        } else {
            // Mostrar explicación si el usuario puede cambiar de opinión
            if (shouldShowRequestPermissionRationale(context, Manifest.permission.CAMERA)) {
                showRationaleDialog.value = true
            } else {
                // Permiso denegado permanentemente
                showPermissionDialog.value = true
            }
        }
    }


    LaunchedEffect(Unit) {
        if (!hasCameraPermission(context)) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    if (showPermissionDialog.value) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog.value = false },
            title = { Text("Permiso requerido") },
            text = { Text("Para tomar fotos necesitas conceder permiso a la cámara. Por favor habilítalo en la configuración de la aplicación.") },
            confirmButton = {
                Button(onClick = {
                    showPermissionDialog.value = false
                    openAppSettings(context)
                }) {
                    Text("Abrir Configuración")
                }
            },
            dismissButton = {
                Button(onClick = { showPermissionDialog.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de explicación (rationale)
    if (showRationaleDialog.value) {
        AlertDialog(
            onDismissRequest = { showRationaleDialog.value = false },
            title = { Text("Permiso necesario") },
            text = { Text("La aplicación necesita acceso a la cámara para poder tomar fotos. ¿Deseas conceder el permiso ahora?") },
            confirmButton = {
                Button(onClick = {
                    showRationaleDialog.value = false
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Conceder Permiso")
                }
            },
            dismissButton = {
                Button(onClick = { showRationaleDialog.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Box(
        modifier = modifier
            .clickable {
                if (hasCameraPermission(context)) {
                    imageUri?.let { uri ->
                        cameraLauncher.launch(uri)
                    }
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (photoPath != null) {
            Image(
                painter = rememberAsyncImagePainter(model = photoPath),
                contentDescription = "Foto tomada",
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_camara),
                    contentDescription = "Cámara",
                    modifier = Modifier.fillMaxWidth()
                )
                if (!hasCameraPermission(context)) {
                    Text(
                        text = "Toque para conceder permisos de cámara",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

// Función de extensión para verificar permisos
private fun hasCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

// Función para abrir configuración de la app
private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

// Función para verificar si debemos mostrar explicación
@SuppressLint("RestrictedApi")
private fun shouldShowRequestPermissionRationale(context: Context, permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(
        (context as ComponentActivity),
        permission
    )
}