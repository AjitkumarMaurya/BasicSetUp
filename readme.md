To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
[![Release](https://jitpack.io/v/AjitkumarMaurya/basicSetUp.svg)]



(https://jitpack.io/#AjitkumarMaurya/basicSetUp)	
	
Step 2. Add the dependency

    dependencies {
	        implementation 'com.github.AjitkumarMaurya:basicSetUp:v1.0'
	}
