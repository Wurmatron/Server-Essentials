node {
	checkout scm
	sh 'gradle spotlessApply'
	sh 'gradle setupCiWorkspace clean buildAndPackage'
	archive 'build/libs/*jar'
}