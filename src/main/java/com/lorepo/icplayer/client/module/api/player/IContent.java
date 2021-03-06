package com.lorepo.icplayer.client.module.api.player;

import com.lorepo.icplayer.client.model.Page;

public interface IContent {

	public int	getPageCount();
	public IPage	getPage(int index);
	public IAddonDescriptor getAddonDescriptor(String addonId);
	public Page findPageByName(String pageName);
	public String getBaseUrl();
	public IChapter getTableOfContents();
	public IChapter getCommonPages();
}
