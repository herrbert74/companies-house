package com.babestudios.companyinfouk.filings.ui.details

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.LabeledDetailCardItem
import com.babestudios.companyinfouk.common.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.filings.ui.filings.createAnnotatedStringDescription
import java.util.Locale

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun FilingDetailsScreen(component: FilingDetailsComp) {

	TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
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
	) { uri -> if (uri != null) component.savePdf(uri) }

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

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_filing_history,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {
			IconButton(onClick = {
				wasCreateDocumentCalled.value = false
				wasCreateDocumentShown.value = false
				component.downloadPdf()
			}) {
				Icon(
					painter = painterResource(R.drawable.ic_picture_as_pdf),
					contentDescription = "Localized description",
					tint = MaterialTheme.colorScheme.onPrimary
				)
			}
		},
		title = stringResource(id = R.string.filing_history_details)
	) {
		if (model.downloadedPdfResponseBody != null && !wasCreateDocumentCalled.value) {
			CheckPermissionAndWriteDocument(
				context, permissionLauncher, model.documentId, createDocumentLauncher
			)
			wasCreateDocumentCalled.value = true
		}
		if (model.savedPdfUri != null && !wasCreateDocumentShown.value) {
			showDocument(model.savedPdfUri!!, context, pdfWillNotSaveMessage)
			wasCreateDocumentShown.value = true
		} else {
			FilingDetailsBody(state, selectedFilingDetails)
		}
	}

}

@Composable
private fun FilingDetailsBody(state: ScrollState, selectedFilingDetails: FilingHistoryItem) {
	Column(
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.verticalScroll(state)
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
			detailStyle = CompaniesTypography.titleLarge,
		)
		LabeledDetailCardItem(
			labelString = stringResource(id = com.babestudios.companyinfouk.common.R.string.pages),
			detailString = String.format(Locale.UK, "%d", selectedFilingDetails.pages)
		)
		Divider(thickness = 1.dp)
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

@Suppress("SwallowedException")
private fun showDocument(pdfUri: Uri, context: Context, pdfWillNotSaveMessage: String) {
	val target = Intent(Intent.ACTION_VIEW)
	target.setDataAndType(pdfUri, "application/pdf")
	target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
	target.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
	val intent = Intent.createChooser(target, "Open File")
	try {
		(context.getActivity() as AppCompatActivity).startActivityWithRightSlide(intent)
	} catch (e: ActivityNotFoundException) {
		Toast.makeText(
			context,
			pdfWillNotSaveMessage,
			Toast.LENGTH_LONG
		).show()
	}
}

fun Context.getActivity(): AppCompatActivity? {
	var currentContext = this
	while (currentContext is ContextWrapper) {
		if (currentContext is AppCompatActivity) {
			return currentContext
		}
		currentContext = currentContext.baseContext
	}
	return null
}

@Preview("FilingDetailsBody Preview")
@Composable
fun FilingDetailsBodyPreview() {
	CompaniesTheme {
		FilingDetailsBody(
			ScrollState(0),
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
