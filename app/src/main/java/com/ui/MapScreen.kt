package com.example.ridesharing.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ridesharing.ui.trips.TripsViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.ui_view.ViewProvider

class MapActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           MapScreen()
        }
    }
}

@Composable
fun MapScreen(viewModel: TripsViewModel = viewModel()) {
  val context = LocalContext.current
    var location by remember { mutableStateOf<Location?>(null) }

  LaunchedEffect(Unit) {
      MapKitFactory.initialize(context)
      if (ActivityCompat.checkSelfPermission(
              context,
              Manifest.permission.ACCESS_FINE_LOCATION
          ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
              context,
              Manifest.permission.ACCESS_COARSE_LOCATION
          ) == PackageManager.PERMISSION_GRANTED
      ) {
          location = viewModel.userLocation.value
     }

  }
    if (location != null) {
      YandexMap(location = location!!)
  }
}

@Composable
fun YandexMap(location: Location) {
   val context = LocalContext.current
    val mapObjectCollection = remember { mutableStateOf<MapObjectCollection?>(null) }
   AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val mapView = com.yandex.mapkit.mapview.MapView(context).apply {
              MapKitFactory.getInstance().onStart()
            }

           val map = mapView.map
           val initialPoint = Point(location.latitude, location.longitude)
            map.move(
               CameraPosition(initialPoint, 15f, 0f, 0f),
               Animation(Animation.Type.SMOOTH, 0f),
               null
           )
         mapObjectCollection.value = map.mapObjects
           addPlacemark(mapObjectCollection.value!!, context, initialPoint)

            mapView
        },
        onRelease = {
          MapKitFactory.getInstance().onStop()
        }
    )
}

fun addPlacemark(mapObjectCollection: MapObjectCollection, context: Context, initialPoint: Point) {
    val viewProvider = ViewProvider(
      android.widget.TextView(context).apply {
        text = "Your location"
     }
   )
     mapObjectCollection.addPlacemark(initialPoint, viewProvider)
}