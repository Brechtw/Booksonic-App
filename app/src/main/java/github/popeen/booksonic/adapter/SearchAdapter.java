/*
  This file is part of Subsonic.
	Subsonic is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	Subsonic is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
	GNU General Public License for more details.
	You should have received a copy of the GNU General Public License
	along with Subsonic. If not, see <http://www.gnu.org/licenses/>.
	Copyright 2015 (C) Scott Jackson
*/

package github.popeen.booksonic.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import github.popeen.booksonic.domain.MusicDirectory;
import github.popeen.booksonic.util.ImageLoader;
import github.popeen.booksonic.util.Util;
import github.popeen.booksonic.view.ArtistView;
import github.popeen.booksonic.dsub.R;
import github.popeen.booksonic.domain.SearchResult;
import github.popeen.booksonic.view.AlbumView;
import github.popeen.booksonic.view.SongView;
import github.popeen.booksonic.view.UpdateView;

import static github.popeen.booksonic.adapter.ArtistAdapter.VIEW_TYPE_ARTIST;

public class SearchAdapter extends SectionAdapter<Serializable> {
	private SearchResult searchResult;
	private ImageLoader imageLoader;
	private boolean largeAlbums;

	public SearchAdapter(Context context, SearchResult searchResult, ImageLoader imageLoader, boolean largeAlbums, OnItemClickedListener listener) {
		this.context = context;
		this.searchResult = searchResult;
		this.imageLoader = imageLoader;
		this.largeAlbums = largeAlbums;

		this.sections = new ArrayList<>();
		this.headers = new ArrayList<>();
		Resources res = context.getResources();
		if(!searchResult.getArtists().isEmpty()) {
			this.sections.add((List<Serializable>) (List<?>) searchResult.getArtists());
			this.headers.add(res.getString(R.string.search_artists));
		}
		if(!searchResult.getAlbums().isEmpty()) {
			this.sections.add((List<Serializable>) (List<?>) searchResult.getAlbums());
			this.headers.add(res.getString(R.string.search_albums));
		}
		if(!searchResult.getSongs().isEmpty()) {
			this.sections.add((List<Serializable>) (List<?>) searchResult.getSongs());
			this.headers.add(res.getString(R.string.search_songs));
		}
		this.onItemClickedListener = listener;
		checkable = true;
	}

	@Override
	public UpdateView.UpdateViewHolder onCreateSectionViewHolder(ViewGroup parent, int viewType) {
		UpdateView updateView = null;
		if(viewType == EntryGridAdapter.VIEW_TYPE_ALBUM_CELL || viewType == EntryGridAdapter.VIEW_TYPE_ALBUM_LINE) {
			updateView = new AlbumView(context, viewType == EntryGridAdapter.VIEW_TYPE_ALBUM_CELL);
		} else if(viewType == EntryGridAdapter.VIEW_TYPE_SONG) {
			updateView = new SongView(context);
		} else if(viewType == VIEW_TYPE_ARTIST) {
			updateView = new ArtistView(context);
		}

		return new UpdateView.UpdateViewHolder(updateView);
	}

	@Override
	public void onBindViewHolder(UpdateView.UpdateViewHolder holder, Serializable item, int viewType) {
		UpdateView view = holder.getUpdateView();
		if(viewType == EntryGridAdapter.VIEW_TYPE_ALBUM_CELL || viewType == EntryGridAdapter.VIEW_TYPE_ALBUM_LINE) {
			AlbumView albumView = (AlbumView) view;
			albumView.setObject((MusicDirectory.Entry) item, imageLoader);
		} else if(viewType == EntryGridAdapter.VIEW_TYPE_SONG) {
			SongView songView = (SongView) view;
			songView.setObject((MusicDirectory.Entry) item, true);
		} else if(viewType == VIEW_TYPE_ARTIST) {
			view.setObject(item);
		}
	}

	@Override
	public int getItemViewType(Serializable item) {
		if(item instanceof MusicDirectory.Entry) {
			MusicDirectory.Entry entry = (MusicDirectory.Entry) item;
			if (entry.isDirectory()) {
				if (largeAlbums) {
					return EntryGridAdapter.VIEW_TYPE_ALBUM_CELL;
				} else {
					return EntryGridAdapter.VIEW_TYPE_ALBUM_LINE;
				}
			} else {
				return EntryGridAdapter.VIEW_TYPE_SONG;
			}
		} else {
			return VIEW_TYPE_ARTIST;
		}
	}

	@Override
	public void onCreateActionModeMenu(Menu menu, MenuInflater menuInflater) {
		if(Util.isOffline(context)) {
			menuInflater.inflate(R.menu.multiselect_media_offline, menu);
		} else {
			menuInflater.inflate(R.menu.multiselect_media, menu);
		}

		menu.removeItem(R.id.menu_remove_playlist);
	}
}
