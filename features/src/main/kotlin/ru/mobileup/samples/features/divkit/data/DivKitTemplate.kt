package ru.mobileup.samples.features.divkit.data

import divkit.dsl.Action
import divkit.dsl.Background
import divkit.dsl.Color
import divkit.dsl.Url
import divkit.dsl.animation
import divkit.dsl.bold
import divkit.dsl.border
import divkit.dsl.center
import divkit.dsl.color
import divkit.dsl.container
import divkit.dsl.core.reference
import divkit.dsl.defer
import divkit.dsl.edgeInsets
import divkit.dsl.end
import divkit.dsl.horizontal
import divkit.dsl.image
import divkit.dsl.matchParentSize
import divkit.dsl.stroke
import divkit.dsl.template
import divkit.dsl.text

object DivKitTemplate {

    object CategoryTitle {
        private const val CATEGORY_TITLE_REF_NAME = "categoryTitle"
        private const val CATEGORY_TITLE_CLICK_REF_NAME = "onCategoryTitleClick"
        private const val CATEGORY_TITLE_BACKGROUND_COLOR_REF_NAME = "categoryTitleBgColor"
        private const val CATEGORY_TITLE_TEXT_COLOR_REF_NAME = "categoryTitleTextColor"

        val titleRef = reference<String>(CATEGORY_TITLE_REF_NAME)
        val clickActionRef = reference<Action>(CATEGORY_TITLE_CLICK_REF_NAME)
        val backgroundColorRef = reference<List<Background>>(CATEGORY_TITLE_BACKGROUND_COLOR_REF_NAME)
        val textColorRef = reference<Color>(CATEGORY_TITLE_TEXT_COLOR_REF_NAME)
        val template by lazy(LazyThreadSafetyMode.NONE) {
            template("productCategory") {
                text(
                    border = border(
                        cornerRadius = 16,
                        stroke = stroke(
                            color = color("#000000")
                        )
                    ),
                    paddings = edgeInsets(8),
                    margins = edgeInsets(4),
                    textAlignmentHorizontal = center,
                )
                    .defer(
                        text = titleRef,
                        action = clickActionRef,
                        background = backgroundColorRef,
                        textColor = textColorRef
                    )
            }
        }
    }

    object Product {
        private const val PRODUCT_TITLE_REF_NAME = "productTitle"
        private const val PRODUCT_TITLE_CLICK_REF_NAME = "onProductTitleClick"
        private const val PRODUCT_WEIGHT_REF_NAME = "productWeight"
        private const val PRODUCT_ICON_URL_REF_NAME = "productIconUrl"

        val titleRef = reference<String>(PRODUCT_TITLE_REF_NAME)
        val weightRef = reference<String>(PRODUCT_WEIGHT_REF_NAME)
        val clickActionRef = reference<Action>(PRODUCT_TITLE_CLICK_REF_NAME)
        val iconUrl = reference<Url>(PRODUCT_ICON_URL_REF_NAME)

        val template by lazy(LazyThreadSafetyMode.NONE) {
            template("product") {
                container(
                    orientation = horizontal,
                    width = matchParentSize(),
                    paddings = edgeInsets(8),
                    margins = edgeInsets(8),
                    border = border(
                        cornerRadius = 4,
                        stroke = stroke(
                            color = color("#000000")
                        )
                    ),
                    contentAlignmentVertical = center,
                    items = listOf(
                        text(
                            fontSize = 20
                        )
                            .defer(text = titleRef),
                        image(
                            border = border(
                                cornerRadius = 24,
                                stroke = stroke(
                                    color = color("#000000")
                                )
                            ),
                        ).defer(imageUrl = iconUrl),
                        text(
                            textAlignmentHorizontal = end,
                            fontWeight = bold
                        )
                            .defer(text = weightRef),
                    ),
                    actionAnimation = animation(
                        duration = 500
                    )
                )
                    .defer(action = clickActionRef)
            }
        }
    }
}
