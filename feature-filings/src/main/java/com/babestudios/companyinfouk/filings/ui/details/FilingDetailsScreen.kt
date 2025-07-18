package com.babestudios.companyinfouk.filings.ui.details

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.babestudios.companyinfouk.common.compose.CollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.LabeledDetailCardItem
import com.babestudios.companyinfouk.common.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.filings.ui.filings.createAnnotatedStringDescription
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.shared.screen.filingdetails.FilingDetailsComp
import com.diamondedge.logging.logging
import com.eygraber.uri.toAndroidUri
import com.eygraber.uri.toKmpUri
import java.util.Locale
import kotlin.math.min

private val log = logging()

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun FilingDetailsScreen(
	component: FilingDetailsComp,
	modifier: Modifier = Modifier,
) {

	val selectedFilingDetails = component.filingHistoryItem
	val model by component.state.subscribeAsState()

	/**
	 * This flag is to prevent calling create document twice when invalidate is called after coming back from Platform
	 * File Chooser, but there is no state change
	 */
	val wasCreateDocumentCalled = remember { mutableStateOf(false) }

	val wasCreateDocumentShown = remember { mutableStateOf(false) }

	val createDocumentLauncher = rememberLauncherForActivityResult(
		ActivityResultContracts.CreateDocument("application/pdf")
	) { uri -> if (uri != null) component.savePdf(uri.toKmpUri()) }

	BackHandler(onBack = { component.onBackClicked() })
	val state = rememberScrollState()

	val context = LocalContext.current
	val pdfWillNotSaveMessage = stringResource(R.string.filing_history_details_wont_save_pdf)

	val permissionLauncher = rememberLauncherForActivityResult(
		ActivityResultContracts.RequestPermission()
	) { isGranted: Boolean ->
		if (isGranted) {
			createDocumentLauncher.launch("${model.documentId}.pdf")
		} else {
			Toast.makeText(
				context,
				pdfWillNotSaveMessage,
				Toast.LENGTH_LONG
			).show()
		}
	}

	CollapsingToolbarScaffold(
		backgroundDrawable = R.drawable.bg_filing_history,
		title = stringResource(R.string.filing_history_details),
		onBackClick = { component.onBackClicked() },
		actions = { progress ->
			IconButton(onClick = {
				wasCreateDocumentCalled.value = false
				wasCreateDocumentShown.value = false
				component.downloadPdf()
			}) {
				Icon(
					painter = painterResource(R.drawable.ic_picture_as_pdf),
					contentDescription = "Localized description",
					tint = Color(
						min(1f, Colors.onSurface.red + 1 - progress),
						min(1f, Colors.onSurface.green + 1 - progress),
						min(1f, Colors.onSurface.blue + 1 - progress)
					)
				)
			}
		},
	) { paddingValues ->
		if (model.downloadedPdfResponseBody != null && !wasCreateDocumentCalled.value) {
			CheckPermissionAndWriteDocument(
				context, permissionLauncher, model.documentId, createDocumentLauncher
			)
			wasCreateDocumentCalled.value = true
		}
		if (model.savedPdfUri != null && !wasCreateDocumentShown.value) {
			showDocument(model.savedPdfUri!!.toAndroidUri(), context, pdfWillNotSaveMessage)
			wasCreateDocumentShown.value = true
		}

		FilingDetailsBody(state, paddingValues, selectedFilingDetails)
	}

}

@Composable
private fun FilingDetailsBody(
	state: ScrollState,
	paddingValues: PaddingValues,
	selectedFilingDetails: FilingHistoryItem,
) {
	Column(
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.padding(paddingValues)
			.verticalScroll(state)
	) {
		LabeledDetailCardItem(
			labelString = stringResource(id = com.babestudios.companyinfouk.common.R.string.date),
			detailString = selectedFilingDetails.date
		)
		LabeledDetailCardItem(
			labelString = stringResource(id = com.babestudios.companyinfouk.common.R.string.category),
			detailString = selectedFilingDetails.category.displayName
		)
		selectedFilingDetails.subcategory?.let {
			LabeledDetailCardItem(
				labelString = stringResource(id = com.babestudios.companyinfouk.common.R.string.subcategory),
				detailString = it
			)
		}
		LabeledDetailCardItem(
			labelString = stringResource(id = com.babestudios.companyinfouk.common.R.string.description),
			detailString = selectedFilingDetails.description.createAnnotatedStringDescription(),
			detailStyle = CompaniesTypography.titleLarge.merge(Colors.onBackground),
		)
		LabeledDetailCardItem(
			labelString = stringResource(id = com.babestudios.companyinfouk.common.R.string.pages),
			detailString = String.format(Locale.UK, "%d", selectedFilingDetails.pages)
		)
		HorizontalDivider(thickness = 1.dp)
	}
}

@Composable
fun CheckPermissionAndWriteDocument(
	context: Context,
	permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
	documentId: String,
	createDocumentLauncher: ManagedActivityResultLauncher<String, Uri?>,
) {
	when (PackageManager.PERMISSION_GRANTED) {
		ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
			createDocumentLauncher.launch("${documentId}.pdf")
		}

		else -> permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
	}
}

private fun showDocument(pdfUri: Uri, context: Context, pdfWillNotSaveMessage: String) {
	val target = Intent(Intent.ACTION_OPEN_DOCUMENT)
	target.setDataAndType(pdfUri, "application/pdf")
	target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
	target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
	val intent = Intent.createChooser(target, "Open File")
	try {
		(context.getActivity() as ComponentActivity).startActivityWithRightSlide(intent)
	} catch (e: ActivityNotFoundException) {
		log.e(e) { "Error showing PDF Document: ${e.message}" }
		Toast.makeText(
			context,
			pdfWillNotSaveMessage,
			Toast.LENGTH_LONG
		).show()
	}
}

fun Context.getActivity(): ComponentActivity? {
	var currentContext = this
	while (currentContext is ContextWrapper) {
		if (currentContext is ComponentActivity) {
			return currentContext
		}
		currentContext = currentContext.baseContext
	}
	return null
}

@Preview
@Composable
private fun FilingDetailsBodyPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
			FilingDetailsBody(
				ScrollState(0),
				PaddingValues(),
				FilingHistoryItem(
					date = "2016-01-31",
					category = Category.CATEGORY_CONFIRMATION_STATEMENT,
					type = "AA",
					description = "**Termination of appointment** of Abdul Gafoor Kannathody Kunjumuihhamed as a director" +
						" on " +
						"2020-04-02",
					pages = 2,
				),
			)
		}
	}
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilingDetailsBodyDarkPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
			FilingDetailsBody(
				ScrollState(0),
				PaddingValues(),
				FilingHistoryItem(
					date = "2016-01-31",
					category = Category.CATEGORY_CONFIRMATION_STATEMENT,
					type = "AA",
					description = "**Termination of appointment** of Abdul Gafoor Kannathody Kunjumuihhamed as a director" +
						" on " +
						"2020-04-02",
					pages = 2,
				),
			)
		}
	}
}
