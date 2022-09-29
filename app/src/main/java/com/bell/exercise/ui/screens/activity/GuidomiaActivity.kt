package com.bell.exercise.ui.screens.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bell.exercise.R
import com.bell.exercise.data.model.CarModel
import com.bell.exercise.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlin.text.Typography.bullet

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<GuidomiaViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bell_NativeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: GuidomiaViewModel) {

    Scaffold(
        topBar = {
            TopBar()
        }
    ) {
        LoadData(viewModel)
    }

}

@Composable
fun LoadData(viewModel: GuidomiaViewModel){

    Column {
        Box(
            Modifier
                .weight(0.3f)
                .fillMaxSize()){

            Image(
                painterResource(R.drawable.tacoma),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(modifier = Modifier
                .wrapContentSize()
                .align(Alignment.BottomStart)
                .padding(bottom = dimensionResource(id = R.dimen.padding_10)))
            {
                Text(text = stringResource(id = R.string.tacoma_2021),
                    color =  colorResource(id = R.color.white),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_16))
                )

                Text(text = stringResource(id = R.string.get_yours_now),
                    color =  colorResource(id = R.color.white),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_16))
                )
            }
        }

        Column(
            Modifier
                .weight(0.7f)
                .fillMaxSize()
                .background(colorResource(id = R.color.white))
        ) {

            Filter(viewModel)

            ExpandableList(viewModel)

        }
    }

}
@Composable
fun Filter(viewModel: GuidomiaViewModel){

    Column(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_10))
            .wrapContentSize()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.size_12)))
            .background(colorResource(id = R.color.dark_gray))
    ) {
        Text(
            text = stringResource(id = R.string.filters),
            color = colorResource(id = R.color.white),
            fontSize = dimensionResource(id = R.dimen.font_18).value.sp,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_10))
        )

        val carModel = viewModel.dropdownItems.value
        val make = mutableListOf<String>()
        val model = mutableListOf<String>()

        make.add(stringResource(id = R.string.any_make))
        model.add(stringResource(id = R.string.any_model))

        for (items in carModel) {
            items.make?.let { make.add(it) }
            items.model?.let { model.add(it) }
        }

        var makeSelectedValue  = Dropdown(list = make, placeholder = stringResource(id = R.string.any_make))
        var modelSelectedValue = Dropdown(list = model, placeholder = stringResource(id = R.string.any_model))

        if(makeSelectedValue== stringResource(id = R.string.any_make)) makeSelectedValue = ""
        if(modelSelectedValue== stringResource(id = R.string.any_model)) modelSelectedValue = ""

        viewModel.filterData(makeSelectedValue,modelSelectedValue)
    }

}

@Composable
fun Dropdown(list: MutableList<String>, placeholder : String) : String{

    var mExpanded by remember { mutableStateOf(false) }

    // Create a string value to store the selected city
    var mSelectedText by remember { mutableStateOf("") }

    var mTextFieldSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero)}

    Column(
        Modifier
            .padding(dimensionResource(id = R.dimen.padding_10))
            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_6)))
            .background(colorResource(id = R.color.white))
    ) {

        // Create an Outlined Text Field
        // with icon and not expanded
        TextField(
            value = mSelectedText,
            onValueChange = {  },
            readOnly = true,
            enabled = false,
            textStyle = TextStyle(fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.dark_gray),
                fontSize = dimensionResource(id = R.dimen.font_16).value.sp),
            placeholder = {
                Text(text = placeholder, fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.font_16).value.sp)
            },
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = colorResource(id = R.color.dark_gray),
                backgroundColor = colorResource(id = R.color.white),
                disabledIndicatorColor = colorResource(id = R.color.transparent)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    mExpanded = !mExpanded
                }
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    mTextFieldSize = coordinates.size.toSize()
                }
        )

        // Create a drop-down menu with list of make/model,
        // when clicked, set the Text Field text as the make/model selected
        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
        ) {
            list.forEach { label ->
                DropdownMenuItem(onClick = {
                    mSelectedText = label
                    mExpanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }
    return mSelectedText
}
@Composable
fun ExpandableList(viewModel: GuidomiaViewModel) {

    val itemIds by viewModel.itemIds.collectAsState()
    val carList = viewModel.carItems.collectAsState().value

    LazyColumn(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.size_2))) {
        itemsIndexed(carList) { index, item ->

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(color = colorResource(id = R.color.white))
            ) {
                ExpandableContainerView(
                    itemModel = item,
                    onClickItem = { viewModel.onItemClicked(index) },
                    expanded = itemIds.contains(index)
                )

                if (index < carList.lastIndex) {
                    Divider(
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_12)),
                        color = colorResource(id = R.color.orange),
                        thickness = dimensionResource(id = R.dimen.size_2)
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    TopAppBar(
        backgroundColor = colorResource(id = R.color.orange)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding()

        ){
            val (title, search) = createRefs()
            Text(
                text = stringResource(id = R.string.app_name),
                color = colorResource(id = R.color.white),
                fontSize = dimensionResource(id = R.dimen.font_18).value.sp,
                fontFamily = abrFatFaceRegularFont,
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.padding_10))
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(search.start)
                        width = Dimension.fillToConstraints
                    }
            )
            IconButton(
                onClick = {

                },
                modifier = Modifier
                    .constrainAs(search) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                Icon(Icons.Filled.Menu,"Menu", tint = colorResource(id = R.color.white))
            }
        }
    }
}

@Composable
fun ExpandableContainerView(itemModel: CarModel, onClickItem: () -> Unit, expanded: Boolean) {
    Box{
        Column {
            ListRow(carModel = itemModel, onClickItem = onClickItem)
            ExpandableView(carModel = itemModel, isExpanded = expanded)
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ListRow(carModel: CarModel, onClickItem: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(
                indication = null, // Removes the ripple effect on tap
                interactionSource = remember { MutableInteractionSource() }, // Removes the ripple effect on tap
                onClick = onClickItem
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .background(colorResource(id = R.color.light_gray))
    ) {
        Image(
            painter = if(carModel.make.equals(stringResource(id = R.string.land_rover))) painterResource(id = R.drawable.range_rover)
            else if(carModel.make.equals(stringResource(id = R.string.alpine))) painterResource(id = R.drawable.alpine_roadster)
            else if(carModel.make.equals(stringResource(id = R.string.bmw))) painterResource(id = R.drawable.bmw_330i)
            else painterResource(id = R.drawable.mercedez_benz_glc),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(0.3f)
                .padding(dimensionResource(id = R.dimen.padding_10))
        )
        Column(
            modifier = Modifier
                .weight(0.7f)
                .align(Alignment.CenterVertically)
        ) {
                Text(text = carModel.make + " " + carModel.model,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(id = R.dimen.font_16).value.sp,
                color = colorResource(id = R.color.dark_gray)
                )

                Text(text = stringResource(id = R.string.price)+ carModel.customerPrice.toString(),
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    fontSize = dimensionResource(id = R.dimen.font_14).value.sp,
                    color =  colorResource(id = R.color.dark_gray)
                )


            RatingBar(rating = carModel.rating ?: 0, modifier = Modifier)
        }
    }
}



@Composable
fun ExpandableView(carModel: CarModel, isExpanded: Boolean) {
    // Opening Animation
    val expandTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeIn(
            animationSpec = tween(300)
        )
    }

    // Closing Animation
    val collapseTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeOut(
            animationSpec = tween(300)
        )
    }

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandTransition,
        exit = collapseTransition
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.light_gray))
            .padding(start = dimensionResource(id = R.dimen.padding_20))) {

           if(carModel.prosList.isNotEmpty())
           BulletList(carModel.prosList, stringResource(id = R.string.pros))

           if(carModel.consList.isNotEmpty())
           BulletList(carModel.consList, stringResource(id = R.string.cons))
        }
    }


}


@ExperimentalComposeUiApi
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int
) {
    val ratingState by remember {
        mutableStateOf(rating)
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_star_18),
                contentDescription = "star",
                modifier = modifier.wrapContentSize(),
                tint = if (i <= ratingState) colorResource(id = R.color.orange) else colorResource(
                    id = R.color.white
                )
            )
        }
    }
}


@Composable
fun BulletList(list: MutableList<String>, type : String) {

    list.removeIf { it == "" }

    Text(
        text = type,
        fontSize = dimensionResource(id = R.dimen.font_16).value.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = R.color.dark_gray),
        modifier = Modifier
            .fillMaxWidth()
    )


    Text(modifier = Modifier
        .fillMaxSize()
        .padding(
            start = dimensionResource(id = R.dimen.padding_6),
            top = dimensionResource(id = R.dimen.padding_6)
        ),
        color = colorResource(id = R.color.orange),
        fontSize = dimensionResource(id = R.dimen.font_18).value.sp,
        fontWeight = FontWeight.Bold,
        text = buildAnnotatedString {
            list.forEach {

                withStyle(style = SpanStyle(colorResource(id = R.color.orange))) {
                        append(bullet)

                        withStyle(style = SpanStyle(color = colorResource(id = R.color.black))) {
                            append("\t\t")
                            append(it)
                        }
                        append("\n")

                }
            }
        }
    )

}