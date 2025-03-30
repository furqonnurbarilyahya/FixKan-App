package com.practice.fixkan.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AutoScrollBanner(images: List<Int>) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll effect
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000) // Ganti slide setiap 3 detik
            val nextIndex = (listState.firstVisibleItemIndex + 1) % images.size
            coroutineScope.launch {
                listState.animateScrollToItem(nextIndex)
            }
        }
    }

    Spacer(Modifier.height(8.dp))

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState) // Agar snapping smooth
        ) {
            items(images) {
                Image(
                    painter = painterResource(it),
                    contentDescription = "banner_home",
                    modifier = Modifier
                        .width(410.dp) // Sesuaikan dengan ukuran yang kamu inginkan
                        .height(150.dp) // Sesuaikan tinggi banner
                        .clip(RoundedCornerShape(8.dp)), // Tambahkan border radius agar lebih smooth
                    contentScale = ContentScale.FillBounds // Pastikan gambar pas sesuai ukuran
                )
            }
        }
    }
}
