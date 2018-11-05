package org.rb.goodspriceandroid;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rb.goodspriceandroid.good.GoodContent;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class GoodFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_POSITION = "position";

    private int mColumnCount = 1;
    private int mSelectedPosition;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GoodFragment() {
    }


    public static GoodFragment newInstance(int columnCount) {
        GoodFragment fragment = new GoodFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_good_list, container, false);
        Log.i("Info",this.getClass().getName()+" onCreateView");
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new GoodRecyclerViewAdapter(GoodContent.ITEMS, mListener));

            // If activity recreated (such as from screen rotate), restore
            // the previous article selection set by onSaveInstanceState().
            // This is primarily necessary when in the two-pane layout.
            if (savedInstanceState != null) {
                mSelectedPosition= savedInstanceState.getInt(ARG_POSITION);
                setSelectedItem(recyclerView,mSelectedPosition);
            }else{
                int position = ((RecyclerView) view).getAdapter().getItemCount() - 1;
                ((RecyclerView) view).scrollToPosition(position);
               ((GoodRecyclerViewAdapter)((RecyclerView) view).getAdapter()).setSelectedPositionAndHighLight(position);

            }
        }

        return view;
    }



    /**
     * Refresh list of item.Usually after item's modification
     */
    public void refreshListView(){
        ((RecyclerView)this.getView()).getAdapter().notifyDataSetChanged();
    }

    /**
     * Try to scroll on item at given position.
     * Calls {@link RecyclerView#scrollToPosition(int) }
     * @param position position to scroll
     */
    public void scrollToPosition(int position){
        RecyclerView view = (RecyclerView) this.getView();

        view.scrollToPosition(position);
    }
    /**
     * Make item selected and highlighted in the list for given position
     * @param position position of item to select ( 0 - n)
     */
    public void setSelectedItem(int position){
        RecyclerView view = (RecyclerView) this.getView();
        GoodRecyclerViewAdapter adapter = (GoodRecyclerViewAdapter) view.getAdapter();
        ////adapter.setSelectedPos(position);
        adapter.setSelectedPositionAndHighLight(position);
        //((MyGoodRecyclerViewAdapter)((RecyclerView)this.getView()).getAdapter()).setSelectedPos(position);
        ////((RecyclerView)this.getView()).getAdapter().notifyDataSetChanged();

    }
    private void setSelectedItem(RecyclerView view, int position) {
        GoodRecyclerViewAdapter adapter = (GoodRecyclerViewAdapter)view.getAdapter();
        adapter.setSelectedPos(position);
        adapter.notifyDataSetChanged();
    }
    public int getSelectedPosition(){
        return ((GoodRecyclerViewAdapter)((RecyclerView)this.getView()).getAdapter()).getSelectedPosition();
    }
    //TODO:remove?
    public GoodContent.GoodItem getSelectedItem(){
       return ((GoodRecyclerViewAdapter)((RecyclerView)this.getView()).getAdapter()).getSelectedItem();
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     * Calls {@link GoodRecyclerViewAdapter#getItemCount()}
     * @return
     */
    public int getItemsCount(){
        GoodRecyclerViewAdapter adapter = (GoodRecyclerViewAdapter) ((RecyclerView)this.getView()).getAdapter();
        return adapter.getItemCount();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        /**
         * Used to send selected item's position  to the attached activity
         * @param selectedPosition
         */
        void onListFragmentInteraction(int selectedPosition);

        /**
         * Used in case of screen rotation when fragment View is recreated again.
         * We have to send new fragment instance to the attached activity.
         * @param listFragment
         */
        void onListFragmentResume(GoodFragment listFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Info",this.getClass().getName()+" onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Info",this.getClass().getName()+" OnResume");
        mListener.onListFragmentResume(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Info",this.getClass().getName()+" onStop");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Info",this.getClass().getName()+" onSaveInstanceState");
        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, getSelectedPosition());
    }
}
