package com.emer.egou.app.parser;

import java.util.ArrayList;
import java.util.List;

import com.emer.egou.app.domain.SearchItem;

public class TmallToTaokeItemParser extends TopJsonParser<List<SearchItem>> {
	@Override
	public List<SearchItem> parserJson(String jsonStr) {
		List<SearchItem> searchItems = new ArrayList<SearchItem>();
		if (jsonStr==null) {
			return null;
		}
		String[] strings = jsonStr.split("\n");
		for (int i = 0; i < strings.length; i++) {
			if (!strings[i].contains("total_results\":0")) {
				System.out.println(strings[i]);
				List<SearchItem> parserJson = new ConvertTaokeItemsParser().parserJson(strings[i]);
				if (parserJson!=null) {
					for (SearchItem searchItem : parserJson) {
						searchItems.add(searchItem);
					}
				}
			}
		}
		return searchItems;
	}

}
