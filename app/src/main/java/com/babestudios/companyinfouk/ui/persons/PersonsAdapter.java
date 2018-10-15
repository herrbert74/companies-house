package com.babestudios.companyinfouk.ui.persons;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.persons.Person;
import com.babestudios.companyinfouk.data.model.persons.Persons;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.PersonsViewHolder> {

	private PersonsRecyclerViewClickListener mItemListener;

	private Persons persons = new Persons();

	PersonsAdapter(Context c, Persons persons) {
		mItemListener = (PersonsRecyclerViewClickListener) c;
		this.persons = persons;
	}

	@Override
	public PersonsViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		View itemLayoutView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.persons_list_item, parent, false);

		return new PersonsViewHolder(itemLayoutView);
	}

	@Override
	public void onBindViewHolder(PersonsViewHolder viewHolder, int position) {
		viewHolder.lblName.setText(persons.items.get(position).name);
		viewHolder.lblNotifiedOn.setText(persons.items.get(position).notifiedOn);
		viewHolder.lblNatureOfControl.setText(persons.items.get(position).naturesOfControl.get(0));
		viewHolder.lblLocality.setText(persons.items.get(position).address.locality);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return persons.items.size();
	}

	class PersonsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.lblName)
		TextView lblName;
		@BindView(R.id.lblNotifiedOn)
		TextView lblNotifiedOn;
		@BindView(R.id.lblNatureOfControl)
		TextView lblNatureOfControl;
		@BindView(R.id.lblLocality)
		TextView lblLocality;

		PersonsViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			mItemListener.personsItemClicked(v, this.getLayoutPosition(), persons.items.get(getLayoutPosition()));
		}
	}

	interface PersonsRecyclerViewClickListener {
		void personsItemClicked(View v, int position, Person person);
	}

	void updateItems(Persons persons) {
		this.persons = persons;
		notifyDataSetChanged();
	}
}
