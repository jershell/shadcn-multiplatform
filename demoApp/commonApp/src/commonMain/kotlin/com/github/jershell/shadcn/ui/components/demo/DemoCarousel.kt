package com.github.jershell.shadcn.ui.components.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.theme.Theme
import com.github.jershell.shadcn.components.card.Card
import com.github.jershell.shadcn.components.card.CardContent
import com.github.jershell.shadcn.components.carousel.Carousel
import com.github.jershell.shadcn.components.carousel.CarouselAlign
import com.github.jershell.shadcn.components.carousel.CarouselContent
import com.github.jershell.shadcn.components.carousel.CarouselItem
import com.github.jershell.shadcn.components.carousel.CarouselNext
import com.github.jershell.shadcn.components.carousel.CarouselOpts
import com.github.jershell.shadcn.components.carousel.CarouselOrientation
import com.github.jershell.shadcn.components.carousel.CarouselPrevious
import com.github.jershell.shadcn.components.carousel.rememberCarouselState
import com.github.jershell.shadcn.components.typography.Muted
import com.github.jershell.shadcn.theme.ColorProps
import com.github.jershell.shadcn.theme.ColorTokens
import com.github.jershell.shadcn.theme.TwDimensions
import com.github.jershell.shadcn.demoapp.generated.resources.Res
import com.github.jershell.shadcn.demoapp.generated.resources.carousel_slide_x_of_x
import org.jetbrains.compose.resources.stringResource

private val DemoItems = (1..5).toList()

@Composable
fun DemoCarousel() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CarouselExample()
        SectionDivider()

        SizesExample()
        SectionDivider()

        VerticalExample()
        SectionDivider()

        ApiExample()
    }
}

@Composable
private fun CarouselExample() {
    val state = rememberCarouselState(pageCount = DemoItems.size)

    Carousel(
        state = state,
        modifier = Modifier.width(320.dp),
    ) {
        CarouselContent { page ->
            CarouselItem { SlideCard(index = page, square = true) }
        }
        CarouselPrevious()
        CarouselNext()
    }
}

@Composable
private fun SizesExample() {
    val state = rememberCarouselState(
        pageCount = DemoItems.size,
        opts = CarouselOpts(align = CarouselAlign.Start),
    )

    Carousel(
        state = state,
        modifier = Modifier.width(320.dp),
    ) {
        CarouselContent(
            basis = 1f / 3f,
            spacing = TwDimensions.gapGapToken2,
        ) { page ->
            CarouselItem { SlideCard(index = page, square = true) }
        }
        CarouselPrevious()
        CarouselNext()
    }
}

@Composable
private fun VerticalExample() {
    val state = rememberCarouselState(
        pageCount = DemoItems.size,
        orientation = CarouselOrientation.Vertical,
    )

    // Reserve 48dp top/bottom for buttons that extend beyond the container
    Carousel(
        state = state,
        modifier = Modifier
            .padding(vertical = TwDimensions.gapGapToken12)
            .width(320.dp)
            .height(220.dp),
    ) {
        CarouselContent(
            fixedPageSize = 96.dp,
            spacing = TwDimensions.gapGapToken2,
        ) { page ->
            CarouselItem(
                modifier = Modifier.fillMaxWidth(),
            ) {
                SlideCard(index = page, square = false)
            }
        }
        CarouselPrevious()
        CarouselNext()
    }
}

@Composable
private fun ApiExample() {
    val state = rememberCarouselState(pageCount = DemoItems.size)
    val currentSlide by remember(state) {
        derivedStateOf { state.currentItem + 1 }
    }
    val totalSlides = DemoItems.size

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(TwDimensions.gapGapToken2),
    ) {
        Carousel(
            state = state,
            modifier = Modifier.width(320.dp),
        ) {
            CarouselContent { page ->
                CarouselItem { SlideCard(index = page, square = true) }
            }
            CarouselPrevious()
            CarouselNext()
        }

        Muted(stringResource(Res.string.carousel_slide_x_of_x, currentSlide, totalSlides))
    }
}

@Composable
private fun SlideCard(
    index: Int,
    square: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = if (square) {
                Modifier.fillMaxWidth().aspectRatio(1f)
            } else {
                Modifier.fillMaxSize()
            },
        ) {
            CardContent(
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    BasicText(
                        text = index.toString(),
                        style = TextStyle(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Theme[ColorProps][ColorTokens.foreground],
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .height(1.dp)
            .background(Theme[ColorProps][ColorTokens.border])
            .alpha(0.45f),
    )
}
