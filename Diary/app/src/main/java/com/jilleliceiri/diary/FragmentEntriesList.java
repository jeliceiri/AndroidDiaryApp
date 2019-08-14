package com.jilleliceiri.diary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.List;

/**
 * FragementEntriesList
 *
 * This class functions as a fragement which displays a list of diary entries.
 *
 * @author Jill Eliceiri
 */

class FragmentEntriesList extends Fragment {

    //instance variables
    private static List<Entry> entries;
    MainActivity mainActivity;
    ViewEntriesActivity viewEntriesActivity;
    Context context = null;
    String[] entries_list;
    String act = null;


    // constructor which accepts arguments and binds a bundle to fragment
    public static FragmentEntriesList newInstance(List<Entry> entriesList, String strArg) {
        FragmentEntriesList fragment = new FragmentEntriesList();
        entries = entriesList;
        Bundle args = new Bundle();
        args.putString("activity", strArg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            act = bundle.getString("activity", "empty");
        }

        try {
            if (act.equals("main")) {
                mainActivity = (MainActivity) getActivity();
            }
            if (act.equals("viewAll")) {
                viewEntriesActivity = (ViewEntriesActivity) getActivity();
            }
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                    "MainActivity must implement callbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        entries_list = new String[entries.size()];
        int index = 0;
        //create a string and append each field, then add this to the String[]
        for (Entry anEntry : entries) {
            StringBuilder newString = new StringBuilder();
            newString.append(anEntry.getId() + " ");
            newString.append("Subject: " + anEntry.getSubject() + "\n");
            newString.append("Content: " + anEntry.getContent() + "\n");
            newString.append("Date Created: " + anEntry.getDate() + "\n");
            newString.append("Date Modified: " + anEntry.getModified() + "\n" + "\n");
            entries_list[index] = newString.toString();
            index++;
        }

        //inflate xml to make the gui
        LinearLayout layout_entries = (LinearLayout) inflater.inflate(R.layout.layout_entries, null);

        //get reference to listview
        ListView listView = (ListView) layout_entries.findViewById(R.id.listViewEntries);

        //use adaptor to populate rows of listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, entries_list);
        listView.setAdapter(adapter);

        //display from the top
        listView.setSelection(0);
        listView.smoothScrollToPosition(0);
        //respond to click on rows
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);
                //get the id
                String delims = " ";
                String[] tokens = selectedItem.split(delims);
                String entryID = tokens[0];

                //create and start new intent and send it the id
                if (act.equals("main")) {
                    mainActivity.startIntent(entryID);
                }
                if (act.equals("viewAll")) {
                    viewEntriesActivity.startIntent(entryID);
                }
            }
        });
        return layout_entries;
    }
}
