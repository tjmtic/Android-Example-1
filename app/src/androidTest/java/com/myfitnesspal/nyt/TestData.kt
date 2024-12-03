package com.myfitnesspal.nyt

object TestData {
  //article_response.json ["web_url"] and ["headline"]["main"] values
  val fakeArticles = listOf(
    ArticleData("Journalist Quits Kenosha Paper in Protest of Its Jacob Blake Rally Coverage","https://www.nytimes.com/2020/08/31/business/media/kenosha-newspaper-editor-quits.html"),
    ArticleData("Big Ten Presidents Voted 11-3 to Cancel Fall Football Season","https://www.nytimes.com/aponline/2020/08/31/sports/ncaafootball/ap-fbc-virus-outbreak-big-ten.html"),
    ArticleData("Joe Biden, Child virus cases, U.S. Open: Your Monday Evening Briefing","https://www.nytimes.com/2020/08/31/briefing/joe-biden-child-infections-us-open.html"),
    ArticleData("In Steve Bannon Case, Prosecutors Have ‘Voluminous’ Emails","https://www.nytimes.com/2020/08/31/nyregion/steve-bannon-we-build-the-wall-kolfage.html"),
    ArticleData("Agency Denies Critical Habitat for Endangered Bumblebee","https://www.nytimes.com/aponline/2020/08/31/us/ap-us-endangered-bumblebee.html"),
    ArticleData("Louisiana Sen. Cassidy Says He's Recovered From COVID-19","https://www.nytimes.com/aponline/2020/08/31/us/ap-us-virus-outbreak-cassidy.html"),
    ArticleData("E.P.A. Relaxes Rules Limiting Toxic Waste From Coal Plants","https://www.nytimes.com/2020/08/31/climate/trump-coal-plants.html"),
    ArticleData("Reds Manager Bell, Others Suspended for Incident With Cubs","https://www.nytimes.com/reuters/2020/08/31/sports/baseball/31reuters-baseball-mlb-cin-chc-suspensions-fines.html"),
    ArticleData("Ignore the C.D.C. and Expand Testing","https://www.nytimes.com/2020/08/31/opinion/cdc-testing-coronavirus.html"),
    ArticleData("AP FACT CHECK: Trump Tweets Distort Truth on National Guard","https://www.nytimes.com/aponline/2020/08/31/us/politics/ap-us-trump-national-guard-fact-check.html"),
  )

  //article_response.json top-level keys
  val fakeResponse = mapOf(
    "status" to "OK",
    "copyright" to "Copyright (c) 2020 The New York Times Company. All Rights Reserved.",
    "response" to mapOf(
      "docs" to fakeArticles
    )
  )
}




