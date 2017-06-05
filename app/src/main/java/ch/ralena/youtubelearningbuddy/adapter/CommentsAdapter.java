package ch.ralena.youtubelearningbuddy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ch.ralena.youtubelearningbuddy.R;
import ch.ralena.youtubelearningbuddy.model.CommentList;
import ch.ralena.youtubelearningbuddy.model.comment.Snippet;
import io.reactivex.functions.Consumer;

/**
 * Created by crater on 04/06/17.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> implements Consumer<CommentList> {
	CommentList commentList;

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commentthread, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.bindView(commentList.getComments().get(position));
	}

	@Override
	public int getItemCount() {
		return commentList == null ? 0 : commentList.getComments().size();
	}

	@Override
	public void accept(CommentList commentList) {
		this.commentList = commentList;
		notifyDataSetChanged();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		ImageView avatar;
		TextView username;
		TextView date;
		TextView comment;

		public ViewHolder(View itemView) {
			super(itemView);
			avatar = (ImageView) itemView.findViewById(R.id.avatarImage);
			username = (TextView) itemView.findViewById(R.id.usernameText);
			date = (TextView) itemView.findViewById(R.id.dateText);
			comment = (TextView) itemView.findViewById(R.id.commentText);
		}

		public void bindView(Snippet snippet) {
			Picasso.with(avatar.getContext())
					.load(snippet.getTopLevelComment().getComment().getAuthorProfileImageUrl())
					.fit()
					.centerCrop()
					.into(avatar);
			username.setText(snippet.getTopLevelComment().getComment().getAuthorDisplayName());
			date.setText(snippet.getTopLevelComment().getComment().getPublishedAt());
			comment.setText(snippet.getTopLevelComment().getComment().getTextOriginal());
		}
	}
}
