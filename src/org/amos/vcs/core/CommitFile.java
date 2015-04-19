package org.amos.vcs.core;

import java.io.File;

public class CommitFile {

	public CommitState commitState;
	public File oldPath;
	public File newPath;
	
	public CommitFile()
	{
		commitState = CommitState.NOTSET;
		oldPath = null;
		newPath = null;
	}
	
	public CommitFile(File oldPath, File newPath, CommitState commitState)
	{
		this.commitState = commitState;
		this.oldPath = oldPath;
		this.newPath = newPath;
	}
}
