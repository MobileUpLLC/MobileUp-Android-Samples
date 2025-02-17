package ru.mobileup.samples.features.collapsing_toolbar.presentation.widgets.custom

class CollapsingToolbarCustomParams {
    val collapsedOffset = 2000
    val contextZIndex = 2f
    val container = Container()
    val background = Background()
    val leadingIcon = LeadingIcon()
    val player = Player()

    class Container {
        val maxHeight = 366f
        val minHeight = 184f
    }

    class Background {
        val imageId = "CollapsingToolbarAdvancedCustomBackgroundImageId"
        val imageZIndex = 0f
        val gradientId = "CollapsingToolbarAdvancedCustomBackgroundGradientId"
        val gradientZIndex = 1f
    }

    class LeadingIcon {
        val id = "CollapsingToolbarAdvancedCustomLeadingIconId"
        val size = 24
        val paddingStart = 20
    }

    class Player {
        val bottomPadding = 16
        val number = Number()
        val photo = Photo()
        val name = Name()
        class Number {
            val id = "CollapsingToolbarAdvancedCustomPlayerNumberId"
            val padding = 16
        }

        class Photo {
            val id = "CollapsingToolbarAdvancedCustomPlayerPhotoId"
            val minSize = 40f
            val maxWidth = 329f
            val maxHeight = 288f
            val maxPaddingStart = 20
            val bottomPaddingStartFraction = 0.8f
        }

        class Name {
            val id = "CollapsingToolbarAdvancedCustomPlayerNameId"
            val paddingStart = 16
        }
    }
}