package com.example.lab2_levashova

object DataStorage {

    fun getVersionsList(): List<Android> {
        return listOf(
            Android(
                "Cupcake",
                R.drawable.cupcake,
                "With early 2009's Android 1.5 Cupcake release, the tradition of Android version names was born. " +
                        "Cupcake introduced numerous refinements to the Android interface, including the first on-screen " +
                        "keyboard — something that'd be necessary as phones moved away from the once-ubiquitous physical" +
                        " keyboard model.\n" +
                        "\n" +
                        "Cupcake also brought about the framework for third-party app widgets, which would quickly turn " +
                        "into one of Android's most distinguishing elements, and it provided the platform's first-ever " +
                        "option for video recording.",
                "April 27, 2009",
                "https://www.youtube.com/watch?v=YJGwmzXzMfw"
            ),

            Android(
                "Donut",
                R.drawable.donut,
                "Android 1.6, Donut, rolled into the world in the fall of 2009. Donut filled in some important holes " +
                        "in Android's center, including the ability for the OS to operate on a variety of different screen sizes " +
                        "and resolutions — a factor that'd be critical in the years to come. It also added support for CDMA " +
                        "networks like Verizon, which would play a key role in Android's imminent explosion.",
                "Septemper 15, 2009",
                "https://www.youtube.com/watch?v=ElvK3nBsh6E"
            ),

            Android(
                "Eclair",
                R.drawable.eclair,
                "Keeping up the breakneck release pace of Android's early years, Android 2.0 Eclair, emerged just " +
                        "six weeks after Donut; its \"point-one\" update, also called Eclair, came out a couple months later. " +
                        "Eclair was the first Android release to enter mainstream consciousness thanks to the original " +
                        "Motorola Droid phone and the massive Verizon-led marketing campaign surrounding it.",
                "October 26, 2009",
                "https://www.youtube.com/watch?v=IvU6g2V_oio"
            ),

            Android(
                "Froyo",
                R.drawable.froyo,
                "Just four months after Android 2.1 arrived, Google served up Android 2.2, Froyo, which revolved " +
                        "largely around under-the-hood performance improvements. Froyo did deliver some important " +
                        "front-facing features, though, including the addition of the now-standard dock at the bottom of " +
                        "the home screen as well as the first incarnation of Voice Actions, which allowed you to perform " +
                        "basic functions like getting directions and making notes by tapping an icon and then speaking a command.\n" +
                        "\n" +
                        "Notably, Froyo also brought support for Flash to Android's web browser — an option that was significant " +
                        "both because of the widespread use of Flash at the time and because of Apple's adamant stance against " +
                        "supporting it on its own mobile devices. Apple would eventually win, of course, and Flash would become far " +
                        "less common. But back when it was still everywhere, being able to access the full web without any black holes " +
                        "was a genuine advantage only Android could offer.",
                "May 20, 2010",
                "https://www.youtube.com/watch?v=yAZYSVr2Bhc"
            ),

            Android(
                "Gingerbread",
                R.drawable.gingerbread,
                "Android's first true visual identity started coming into focus with 2010's Gingerbread release. Bright green " +
                        "had long been the color of Android's robot mascot, and with Gingerbread, it became an integral part of the " +
                        "operating system's appearance. Black and green seeped all over the UI as Android started its slow march toward " +
                        "distinctive design.",
                "December 6, 2010",
                "https://www.youtube.com/watch?v=Jx3pdWBlZ34"
            ),

            Android(
                "Honeycomb",
                R.drawable.honeycomb,
                "2011's Honeycomb period was a weird time for Android. Android 3.0 came into the world as a tablet-only " +
                        "release to accompany the launch of the Motorola Xoom, and through the subsequent 3.1 and 3.2 updates," +
                        " it remained a tablet-exclusive (and closed-source) entity.\n" +
                        "\n" +
                        "Under the guidance of newly arrived design chief Matias Duarte, Honeycomb introduced a dramatically" +
                        "reimagined UI for Android. It had a space-like \"holographic\" design that traded the platform's trademark " +
                        "green for blue and placed an emphasis on making the most of a tablet's screen space.",
                "February 22, 2011",
                "https://www.youtube.com/watch?v=hPUGNCIozp0"
            ),

            Android(
                "Jelly Bean",
                R.drawable.jelly,
                "Spread across three impactful Android versions, 2012 and 2013's Jelly Bean releases took ICS's fresh " +
                        "foundation and made meaningful strides in fine-tuning and building upon it. The releases added plenty " +
                        "of poise and polish into the operating system and went a long way in making Android more inviting " +
                        "for the average user.\n" +
                        "\n" +
                        "Visuals aside, Jelly Bean brought about our first taste of Google Now — the spectacular " +
                        "predictive-intelligence utility that's sadly since devolved into a glorified news feed. It gave us " +
                        "expandable and interactive notifications, an expanded voice search system, and a more advanced system " +
                        "for displaying search results in general, with a focus on card-based results that attempted to answer " +
                        "questions directly.\n" +
                        "\n" +
                        "Multiuser support also came into play, albeit on tablets only at this point, and an early version of " +
                        "Android's Quick Settings panel made its first appearance. Jelly Bean ushered in a heavily hyped system " +
                        "for placing widgets on your lock screen, too — one that, like so many Android features over the years, " +
                        "quietly disappeared a couple years later.",
                "July 9, 2012",
                "https://www.youtube.com/watch?v=poSFV9_abcw"
            ),

            Android(
                "Kitkat",
                R.drawable.kitkat,
                "Late-2013's KitKat release marked the end of Android's dark era, as the blacks of Gingerbread and the " +
                        "blues of Honeycomb finally made their way out of the operating system. Lighter backgrounds and more " +
                        "neutral highlights took their places, with a transparent status bar and white icons giving the OS a" +
                        " more contemporary appearance.\n" +
                        "\n" +
                        "Android 4.4 also saw the first version of \"OK, Google\" support — but in KitKat, the hands-free " +
                        "activation prompt worked only when your screen was already on and you were either at your home screen " +
                        "or inside the Google app.\n" +
                        "\n" +
                        "The release was Google's first foray into claiming a full panel of the home screen for its services, " +
                        "too — at least, for users of its own Nexus phones and those who chose to download its first-ever " +
                        "standalone launcher.",
                "October 31, 2013",
                "https://www.youtube.com/watch?v=sONcojECWXs"
            ),

            Android(
                "Lollipop",
                R.drawable.lollipop,
                "Google essentially reinvented Android — again — with its Android 5.0 Lollipop release in the fall of 2014. " +
                        "Lollipop launched the still-present-today Material Design standard, which brought a whole new look " +
                        "that extended across all of Android, its apps and even other Google products.\n" +
                        "\n" +
                        "The card-based concept that had been scattered throughout Android became a core UI pattern — one that " +
                        "would guide the appearance of everything from notifications, which now showed up on the lock screen for " +
                        "at-a-glance access, to the Recent Apps list, which took on an unabashedly card-based appearance.",
                "November 12, 2014",
                "https://www.youtube.com/watch?v=ZkGSR0Q492g"
            ),

            Android(
                "Marshmallow",
                R.drawable.marshmallow,
                "In the grand scheme of things, 2015's Marshmallow was a fairly minor Android release — one that " +
                        "seemed more like a 0.1-level update than anything deserving of a full number bump. But it started " +
                        "the trend of Google releasing one major Android version per year and that version always receiving " +
                        "its own whole number.\n" +
                        "\n" +
                        "Marshmallow's most attention-grabbing element was a screen-search feature called Now On Tap — " +
                        "something that, as I said at the time, had tons of potential that wasn't fully tapped. Google " +
                        "never quite perfected the system and ended up quietly retiring its brand and moving it out of the " +
                        "forefront the following year.",
                "October 5, 2014",
                "https://www.youtube.com/watch?v=U9tw5ypqEN0&list=PLWz5rJ2EKKc9VBBpeGykiTh8HQ1fSBnk-"
            ),

            Android(
                "Nougat",
                R.drawable.nougat,
                "Google's 2016 Android Nougat releases provided Android with a native split-screen mode, a " +
                        "new bundled-by-app system for organizing notifications, and a Data Saver feature. Nougat added " +
                        "some smaller but still significant features, too, like an Alt-Tab-like shortcut for snapping between apps.",
                "August 22, 2016",
                "https://www.youtube.com/watch?v=6JKve8YLwu4"
            ),

            Android(
                "Oreo",
                R.drawable.oreo,
                "Android Oreo added a variety of niceties to the platform, including a native picture-in-picture mode, " +
                        "a notification snoozing option, and notification channels that offer fine control over how apps can alert you.",
                "August 21, 2017",
                "https://www.youtube.com/watch?v=7kD0ZYzJbYo"
            ),

            Android(
                "Pie",
                R.drawable.pie,
                "The freshly baked scent of Android Pie, a.k.a. Android 9, wafted into the Android ecosystem in August of 2018. " +
                        "Pie's most transformative change was its hybrid gesture/button navigation system, which traded Android's " +
                        "traditional Back, Home, and Overview keys for a large, multifunctional Home button and a small Back button " +
                        "that appeared alongside it as needed.",
                "August 6, 2018",
                "https://www.youtube.com/watch?v=ZXyvgHJ6c4w"
            ),

            Android(
                "Android 10",
                R.drawable.android_ten,
                "Google released Android 10 — the first Android version to shed its letter and be known simply by a number, " +
                        "with no dessert-themed moniker attached — in September of 2019. Most noticeably, the software brings about a " +
                        "totally reimagined interface for Android gestures, this time doing away with the tappable Back button " +
                        "altogether and relying on a completely swipe-driven approach to system navigation. (If you so choose, that" +
                        " is; unlike Pie, Android 10 also includes the traditional Android three-button navigation system as an " +
                        "option on all phones.)",
                "September 3, 2019",
                "https://www.youtube.com/watch?v=yOMLgWQ3ckQ"
            ),

            Android(
                "Android 11",
                R.drawable.android_el,
                "Android 11, launched at the start of September 2020, is a pretty substantial Android update both under " +
                        "the hood and on the surface. The version's most significant changes revolve around privacy: " +
                        "The update builds upon the expanded permissions system introduced in Android 10 and adds in the " +
                        "ability for users to grant apps certain permissions — those related to location access, camera access, " +
                        "and microphone access — only on a limited, single-use basis.",
                "September 8, 2020",
                "https://www.youtube.com/watch?v=fnkFOhA7FC4"
            )
        )
    }
}
