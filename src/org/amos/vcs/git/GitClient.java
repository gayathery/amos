package org.amos.vcs.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.amos.vcs.core.CommitFile;
import org.amos.vcs.core.CommitState;
import org.amos.vcs.interfaces.VcsClient;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.io.DisabledOutputStream;

public class GitClient implements VcsClient{

	Git git;
	String repositoryURI;
	Repository repo;
	Boolean isConnected;
	public GitClient(String repositoryURI)
	{
		this.repositoryURI = repositoryURI;
		isConnected = false;
		repo = null;
		git = null;
	}
	private GitClient()
	{}
	public void connect()
	{
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			repo = builder.setGitDir(new File(repositoryURI)).setMustExist(true).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		git = new Git(repo);
		isConnected = true;
	}
	@Override
	public Iterator<String> getBranchList() {
		ArrayList<String> branchList = new ArrayList<String>();
		try {
			List<Ref> branches = git.branchList().call();
			
			for (Ref branch : branches) {
				branchList.add(branch.getName());
			}
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return branchList.iterator();
	}
	@Override
	public Iterator<String> getCommitList() {
		ArrayList<String> commitList = new ArrayList<String>();
		Iterable<RevCommit> commits = null;
		try {
			commits = git.log().all().call();
		} catch (GitAPIException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for (RevCommit commit : commits) {
        	commitList.add(commit.getId().getName());
        }
		return commitList.iterator();
	}
	@Override
	public Iterator<CommitFile> getCommitFiles(String commitID) {
		
		ArrayList<CommitFile> commitFilesList = new ArrayList<CommitFile>();
		Iterable<RevCommit> commits = null;
		try {
			commits = git.log().all().call();
		} catch (GitAPIException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (RevCommit commit : commits) {
        	if(!commit.getId().getName().equals(commitID))
        		continue;
        	
        	RevCommit parent;
			try {
				parent = commit.getParent(0);
			} catch (ArrayIndexOutOfBoundsException e1) {
				
				return commitFilesList.iterator();
			}
        	DiffFormatter dif = new DiffFormatter(DisabledOutputStream.INSTANCE);
        	dif.setRepository(repo);
        	dif.setDiffComparator(RawTextComparator.DEFAULT);
        	dif.setDetectRenames(true);
        	List<DiffEntry> diffs = null;
			try {
				diffs = dif.scan(parent.getTree(), commit.getTree());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	for (DiffEntry diff : diffs) {
        		CommitState commitState;
        		commitState = CommitState.NOTSET;
        		switch(diff.getChangeType())
        		{
        		case ADD:
        			commitState = CommitState.ADDED;
        			break;
        		case MODIFY:
        			commitState = CommitState.MODIFIED;
        			break;
        		case RENAME:
        			commitState = CommitState.RENAMED;
        			break;
        		case DELETE:
        			commitState = CommitState.DELETED;
        			break;
        			default:
        				break;
        		}
        		CommitFile commitFile = new CommitFile(new File(diff.getOldPath()),new File(diff.getNewPath()),commitState);
        	    commitFilesList.add(commitFile);
        		//System.out.println(MessageFormat.format("({0} {1} {2}", diff.getChangeType().name(), diff.getNewMode().getBits(), diff.getNewPath()));
        }
        	return commitFilesList.iterator();
		
	}
		return commitFilesList.iterator();
}
}
