package com.babestudios.companieshouse;

import com.babestudios.companieshouse.injection.ApplicationComponent;
import com.babestudios.companieshouse.ui.charges.ChargesPresenter;
import com.babestudios.companieshouse.ui.company.CompanyPresenter;
import com.babestudios.companieshouse.ui.company.CompanyPresenterTest;
import com.babestudios.companieshouse.ui.filinghistory.FilingHistoryPresenter;
import com.babestudios.companieshouse.ui.filinghistorydetails.FilingHistoryDetailsPresenter;
import com.babestudios.companieshouse.ui.insolvency.InsolvencyPresenter;
import com.babestudios.companieshouse.ui.officerappointments.OfficerAppointmentsPresenter;
import com.babestudios.companieshouse.ui.officers.OfficersPresenter;
import com.babestudios.companieshouse.ui.persons.PersonsPresenter;
import com.babestudios.companieshouse.ui.search.SearchPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {TestApplicationModule.class})
public interface TestApplicationComponent extends ApplicationComponent {
	void inject(CompanyPresenterTest companyPresenterTest);
	void inject(CompanyPresenter companyPresenter);
	void inject(SearchPresenter searchPresenter);

	void inject(InsolvencyPresenter insolvencyPresenter);

	void inject(ChargesPresenter chargesPresenter);

	void inject(FilingHistoryPresenter filingHistoryPresenter);

	void inject(OfficersPresenter officersPresenter);

	void inject(PersonsPresenter personsPresenter);

	void inject(FilingHistoryDetailsPresenter filingHistoryDetailsPresenter);

	void inject(OfficerAppointmentsPresenter officerAppointmentsPresenter);
}
