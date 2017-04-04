Challenge
===========================================
Challenge is an Android application simply displays 50 closest Foursquare venues using user location.

<h2>Contents</h2>

These are the Android samples for Google Play game services.

* **BasicSamples** - a set of basic samples, including a convenience library (BaseGameUtils):

    * **BaseGameUtils**. Utilities used on all samples, which you can use in your projects too. This is not a stand-alone sample, it's a library project.

    * **ButtonClicker2000**. Represents the new generation in modern button-clicking excitement. A simple multiplayer game sample that shows how to set up the Google Play real-time multiplayer API, invite friends, automatch, accept invitations, use the waiting room UI, send and receive messages and other multiplayer topics.

    * **CollectAllTheStars2**. Demonstrates how to use the Snapshots feature to save game data. The sample signs the user in, synchronizes their data from a named Snapshot, then updates the UI to reflect the game state saved in the Snapshot.

    * **TrivialQuest2**. Demonstrates how to use the Events and Quests features of Google Play Services. The sample presents a sign in button and four buttons to simulate killing monsters in-game. When you click the buttons, an event is
created and sent to Google Play Games to track what the player is doing in game.

    * **TrivialQuest**. The simplest possible single-player game. Shows how to sign in and how to unlock one achievement. Sign-in and click the button to win the game. Are you ready for this epic adventure?

    * **TypeANumber**. Shows leaderboards and achievements. In this exciting game, you type the score you think you deserve. But wait! There is a twist. If you are playing in easy mode, you get the score you requested. However, if you are playing in hard mode, you only get half! (tough game, we know).

   * **SkeletonTbmp** A trivial turn-based-multiplayer game.  In this thrilling game, you can invite many friends, then send a shared gamestate string back and forth until someone finishes, cancels, or the second-to-last player leaves.

   * **BeGenerous** Send gifts and game requests to other players of BeGenerous.

   * **SavedGames**. Demonstrates the used of Saved Games (Snapshots) feature and how to migrate data from the older Cloud Save (AppState) service to the newer service.  The sample allows the user to save/load data from both Cloud Save and Saved Games.

**Note:** the samples that have corresponding counterparts for iOS and web (particularly, CollectAllTheStars and TypeANumber) are compatible across the platforms. This means that you can play some levels on CollectAllTheStars on your Android device, and then pick up your iOS device and continue where you left off! For TypeANumber, you will see your achievements and leaderboards on all platforms, and progress obtained on one will be reflected on the others.

<h2>How to run a sample</h2>

1. Open Android Studio and launch the Android SDK manager from it (Tools | Android | SDK Manager)
1. Check that these two components are installed and updated to the latest version. Install or upgrade
   them if necessary.
   1. *Android SDK Platform Tools*
   2. *Android Support Library*
   2. *Google Play Services*
   3. *Google Repository*
1. Return to Android Studio and select *Import Project*
1. Select the **Challenge** directory
1. Select "Import from existing model - Gradle"

<h2>Support</h2>

*Challenge is written by [Yigit Cagri Akkaya](http://plus.google.com/+BrunoOliveira).* Feel free to pester me to fix anything that's broken!
