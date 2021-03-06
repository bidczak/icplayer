package com.lorepo.icplayer.client.module.api.player;

import java.util.List;

import com.lorepo.icplayer.client.model.Page;



public interface IChapter extends IContentNode {
	boolean add(IContentNode node);
	public IContentNode get(int index);
	public int size();
	public List<Page> getAllPages();
}
