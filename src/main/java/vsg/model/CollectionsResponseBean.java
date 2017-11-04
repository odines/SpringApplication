package vsg.model;

import java.util.Arrays;

/**
 * Created by Denis Orlov.
 */
public class CollectionsResponseBean {
	private Items[] mItems;

	public Items[] getItems() {
		return mItems;
	}

	public void setItems(Items[] pItems) {
		mItems = pItems;
	}

	@Override
	public String toString() {
		return "CollectionsResponseBean [items = " + Arrays.toString(mItems) + "]";
	}
}
