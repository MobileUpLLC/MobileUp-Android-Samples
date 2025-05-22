package ru.mobileup.samples.features.divkit.presentation.examples_list

import com.yandex.div.core.view2.Div2View
import kotlinx.coroutines.flow.StateFlow

interface DivKitExamplesListComponent {

    val content: StateFlow<Div2View?>

    enum class Action(val url: String, val jsonName: String) {
        ActionAnimation("div-action://action/animation", "action_animation"),
        TransitionChange("div-action://transition/change", "transition_change"),
        TransitionInOut("div-action://transition/in/out", "transition_in_out"),
        Alpha("div-action://alpha", "alpha"),
        Background("div-action://background", "background"),
        Border("div-action://border", "border"),
        SizeTypes("div-action://size/types", "size_types"),
        SizeUnits("div-action://size/units", "size_units"),
        ContainerAlignment("div-action://container/alignment", "container_alignment"),
        ContainerHorizontal("div-action://container/horizontal", "container_horizontal"),
        ContainerOverlap("div-action://container/overlap", "container_overlap"),
        ContainerVertical("div-action://container/vertical", "container_vertical"),
        Gallery("div-action://gallery", "gallery"),
        GridSpans("div-action://grid/spans", "grid_spans"),
        GridWeight("div-action://grid/weight", "grid_weight"),
        ImagePlaceholders("div-action://image/placeholders", "image_placeholders"),
        ImageScale("div-action://image/scale", "image_scale"),
        ImageSvg("div-action://image/svg", "image_svg"),
        TextInput("div-action://text/input", "text_input"),
        PageWithIndicator("div-action://pager/with/indicator", "pager_with_indicator"),
        Select("div-action://select", "select"),
        Separator("div-action://separator", "separator"),
        SliderCustomization("div-action://slider/customization", "slider_customization"),
        ThumbValueActions("div-action://thumb/value/actions", "thumb_value_actions"),
        StateButton("div-action://state/button", "state_button"),
        StateEmpty("div-action://state/empty", "state_empty"),
        Tabs("div-action://tabs", "tabs"),
        TextProperties("div-action://text/properties", "text_properties"),
        TextRanges("div-action://text/ranges", "text_ranges"),
        Video("div-action://video", "video"),
        SignUp("div-action://sign/up", "sign_up"),
        GoodList("div-action://goods/list", "goods_list"),
        Unknown("", "");

        companion object {
            fun fromUrl(url: String?) = when (url) {
                ActionAnimation.url -> ActionAnimation
                TransitionChange.url -> TransitionChange
                TransitionInOut.url -> TransitionInOut
                Alpha.url -> Alpha
                Background.url -> Background
                Border.url -> Border
                SizeTypes.url -> SizeTypes
                SizeUnits.url -> SizeUnits
                ContainerAlignment.url -> ContainerAlignment
                ContainerHorizontal.url -> ContainerHorizontal
                ContainerOverlap.url -> ContainerOverlap
                ContainerVertical.url -> ContainerVertical
                Gallery.url -> Gallery
                GridSpans.url -> GridSpans
                GridWeight.url -> GridWeight
                ImagePlaceholders.url -> ImagePlaceholders
                ImageScale.url -> ImageScale
                ImageSvg.url -> ImageSvg
                TextInput.url -> TextInput
                PageWithIndicator.url -> PageWithIndicator
                Select.url -> Select
                Separator.url -> Separator
                SliderCustomization.url -> SliderCustomization
                ThumbValueActions.url -> ThumbValueActions
                StateButton.url -> StateButton
                StateEmpty.url -> StateEmpty
                Tabs.url -> Tabs
                TextProperties.url -> TextProperties
                TextRanges.url -> TextRanges
                Video.url -> Video
                SignUp.url -> SignUp
                GoodList.url -> GoodList
                else -> Unknown
            }
        }
    }

    sealed interface Output {
        data class DetailsRequested(
            val title: String,
            val jsonName: String
        ) : Output
    }
}