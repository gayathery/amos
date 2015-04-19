package org.amos.vcs.interfaces;

import java.util.Iterator;

import org.amos.vcs.core.CommitFile;

public interface VcsClient {

	public void connect();
	public Iterator<String> getBranchList();
	public Iterator<String> getCommitList();
	public Iterator<CommitFile> getCommitFiles(String commitID);
}
