Term Explorer
=============

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0) ![This script was last tested in Nuix 8.6](https://img.shields.io/badge/Script%20Tested%20in%20Nuix-8.6-green.svg)

View the GitHub project [here](https://github.com/Nuix/Term-Explorer) or download the latest release [here](https://github.com/Nuix/Term-Explorer/releases).

# Overview

**Written By:** Jason Wells

Use this script to help you explore the terms present in a Nuix case.

![image](https://user-images.githubusercontent.com/11775738/57660919-8993f080-759d-11e9-8063-4bdd967be558.png)

Begin by providing one or more *single term wild card expressions* and/or fuzzy expressions.  Separate multiple expressions with spaces.  The provided expressions are then used to find related terms present in the current Nuix case.

Wild card expressions support `*` (0 or more characters) or `?` (1 character).

| Expression | Example Term Matches|
|------------|---------------------|
| `cat*`     | `cat`, `catalina`, `catch`, `catching`, `category`, `caterpillar` |
| `*th`      | `depth`, `path`, `width`, `with`                                  |
| `ca?`      | `car`, `cat`, `can`                                               |
| `ca??`     | `call`, `card`, `case`, `cats`                                    |
| `h???th`   | `health`, `hearth`                                                |

Fuzzy terms expressions take the form `TERM~SIMILARITY`, with `SIMILARITY` being a value between `0.0` (not similar) and `1.0` (exactly the same).  Providing a blank value for `SIMILARITY` (i.e. `car~`) is the same as providing `0.5` (i.e. `car~0.5`).

| Expression | Example Term Matches|
|------------|---------------------|
| `cat~0.5`   | `act`, `at`, `bat`, `can`, `car`, `cat`, `cats`, `coat`, `eat`, `hat`, `nat`, `pat`, `rat`, `sat`                              |
| `jason~0.5` | `bison`, `jackson`, `jadon`, `jalyn`, `jaron`, `jason`, `jast`, `jevon`, `json`, `larson`, `olson`, `saison`, `samson`, `wasn` |
| `jason~0.8` | `jadon`, `jaron`, `jasen`, `jason`, `javon`, `jayson`, `mason`                                                                 |

Fuzzy similarity can be calculated using several difference methods:
- **Nuix**: Determines what terms Nuix resolved the given fuzzy search term to.  Running the given fuzzy query expression in the Nuix search bar is expected to return the same results as when you take the matched terms found by this script and join them into an OR query (i.e. `jadon OR jaron OR jasen OR javon OR ...` == `jason~0.8`).  Note that this approach most accurately represents how Nuix would resolve the given fuzzy search to terms, but also takes longer because it is resolved against each responsive item individually.  That means the more items responsive to the given fuzzy search, the longer this approach can take to resolve!
- **[Levenstein Distance](https://lucene.apache.org/core/6_6_0/suggest/org/apache/lucene/search/spell/LevensteinDistance.html)** - Uses Lucene's built in Levenshstein distance string comparison to filter provided fuzzy term against case terms list.  [Levenshstein distance](https://en.wikipedia.org/wiki/Levenshtein_distance) compares 2 strings by determining the number of edits (insertions, deletions and substitutions) needed to change one string to the other.  The Lucene method scales the resulting edit distance to a range between `0.0` and `1.0`.  The script resolves this type of fuzzy search against the case terms list.
- **[Jaro-Winkler](https://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance)** - Uses Lucene's built in Jaro-Winkler string comparison method.
- **[NGram Distance](https://lucene.apache.org/core/6_6_0/suggest/org/apache/lucene/search/spell/NGramDistance.html)** - Uses Lucene's built in [NGram distance](https://webdocs.cs.ualberta.ca/~kondrak/papers/spire05.pdf) string comparison method.  The script resolves this type of fuzzy search against the case terms list.

Resulting matched terms can be exported to a CSV or added to a running collection of terms you build up (the right hand table).  This collection of terms you have selected can then be used in different ways including generating a query from the terms or saving them to a file.

# Getting Started

## Setup

Begin by downloading the latest release of this code.  Extract the contents of the archive into your Nuix scripts directory.  In Windows the script directory is likely going to be either of the following:

- `%appdata%\Nuix\Scripts` - User level script directory
- `%programdata%\Nuix\Scripts` - System level script directory

# Cloning this Repository

This script relies on 2 JAR files, not included in this repository (but is included in [releases](https://github.com/Nuix/Term-Explorer/releases)).

`TermExplorerGUI.jar` provides the user interface of the script.  The source code of this JAR file is included in this repository in the [Java](https://github.com/Nuix/Term-Explorer/tree/master/Java) sub-directory.

The other JAR file is `SuperUtilities.jar` which includes the class [TermExpander]() which does the work of taking a given term expression and resolving it to the appropriate related terms.  You can head over to the [SuperUtilities repository](https://github.com/Nuix/SuperUtilities) and either download a copy of the source and build it yourself or [download an already built release JAR](https://github.com/Nuix/SuperUtilities/releases).

# License

```
Copyright 2020 Nuix

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```