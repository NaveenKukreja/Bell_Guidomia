package com.bell.exercise

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bell.exercise.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlin.text.Typography.bullet

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ExpandableListViewModel>()

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
fun MainScreen(viewModel: ExpandableListViewModel) {

    Scaffold(
        topBar = {
            TopBar()
        }
    ) {
        LoadData(viewModel)
    }

}

@Composable
fun LoadData(viewModel: ExpandableListViewModel){

    Column {
        Row(
            Modifier
                .weight(0.3f)
                .fillMaxSize()){

            Image(
                painterResource(R.drawable.tacoma),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            Modifier
                .weight(0.3f)
                .fillMaxSize()
                .background(colorResource(id = R.color.white))
        ) {

          Filter(viewModel)

        }
        Column(
            Modifier
                .weight(0.4f)
                .fillMaxSize()
                .background(colorResource(id = R.color.white))
        ) {

            ExpandableList(viewModel)

        }
    }

}

@Composable
fun Filter(viewModel: ExpandableListViewModel){

        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_10))
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
                .background(
                    colorResource(id = R.color.dark_gray)
                )
        ){
            Text(text = stringResource(id = R.string.filters),
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color =  colorResource(id = R.color.white),
                modifier = Modifier.padding(10.dp)
            )

            val carModel = viewModel.items.value
            val make = mutableListOf<String>()
            val model = mutableListOf<String>()

            for(items in carModel){
                items.make?.let { make.add(it) }
                items.model?.let { model.add(it) }
            }

            Dropdown(list = make, placeholder = stringResource(id = R.string.any_make))

            Dropdown(list = model, placeholder = stringResource(id = R.string.any_model))

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
            .background(colorResource(id = R.color.white))
    ) {

        // Create an Outlined Text Field
        // with icon and not expanded
        TextField(
            value = mSelectedText,
            onValueChange = { mSelectedText = it },
            readOnly = true,
            enabled = false,
            placeholder = {
                Text(text = placeholder)
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = colorResource(id = R.color.white)),
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
fun ExpandableList(viewModel: ExpandableListViewModel) {

    val itemIds by viewModel.itemIds.collectAsState()

    LazyColumn(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.margin_3))) {
        itemsIndexed(viewModel.items.value) { index, item ->
            ExpandableContainerView(
                itemModel = item,
                onClickItem = { viewModel.onItemClicked(index) },
                expanded = itemIds.contains(index)
            )

            if (index < viewModel.items.value.lastIndex)
                Divider(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_10)), color = colorResource(id = R.color.orange), thickness = 2.dp)
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
                modifier = Modifier
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
        verticalAlignment = Alignment.CenterVertically,
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
            painter = painterResource(id = R.drawable.bmw_330i),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(0.3f)
                .padding(dimensionResource(id = R.dimen.padding_10))
        )
        Column(
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.margin_3))
                .weight(0.7f)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = carModel.make + " " + carModel.model,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    fontSize = (dimensionResource(id = R.dimen.font_18).value.sp)
                ),
                color = colorResource(id = R.color.dark_gray)
            )
                Text(text = carModel.customerPrice.toString(),
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color =  colorResource(id = R.color.dark_gray),
                    modifier = Modifier.padding(
                        end = 25.dp
                    )
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
        Column(modifier = Modifier.background(colorResource(R.color.light_gray))) {
           Text(
                text = stringResource(id = R.string.pros),
                fontSize = 16.sp,
                color = colorResource(id = R.color.black),
                modifier = Modifier
                    .fillMaxWidth()
            )

           BulletList(carModel.prosList)

            Text(
                text = stringResource(id = R.string.cons),
                fontSize = 16.sp,
                color = colorResource(id = R.color.black),
                modifier = Modifier
                    .fillMaxWidth()
            )

            BulletList(carModel.consList)
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
                painter = painterResource(id = R.drawable.ic_baseline_star_24),
                contentDescription = "star",
                modifier = modifier
                    .wrapContentSize(),
                tint = if (i <= ratingState) colorResource(id = R.color.orange) else colorResource(
                    id = R.color.white
                )
            )
        }
    }
}


@Composable
fun BulletList(list: MutableList<String>) {

    list.removeIf { it == "" }

    Text(modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.orange),
        text = buildAnnotatedString {
            list.forEach {

                withStyle(style = SpanStyle(colorResource(id = R.color.orange), fontSize = 20.sp)) {
                    append(bullet)

                    withStyle(style = SpanStyle(color = colorResource(id = R.color.black), fontSize = 20.sp)) {
                        append("\t\t")
                        append(it)
                    }
                    append("\n")

                }
            }
        }
    )

}