# Scoreboard

This project implements a simple scoreboard service library for managing ongoing and finished matches, allowing the user to start new matches, update scores, finish matches, and view match summaries.

## Features

- **Start a new match**: Begin a new match by specifying home and away teams.
- **Update scores**: Update the scores of ongoing matches.
- **Finish a match**: Mark a match as finished and move it from the ongoing matches list to the finished matches list.
- **Match summary**: Get a summary of ongoing matches sorted by their total score 

## Assumptions and Design Decisions

- **No Match ID**: Each match is identified by its position in the ongoing matches map (the match ID is auto-generated sequentially).
- **Match Teams Validation**: A team is prevented from playing against itself, and it's ensured teams are not used in multiple ongoing matches.
- **Exception Handling**: The service includes custom exceptions for scenarios like:
    - Trying to start a match between the same teams.
    - Trying to update or finish a match that does not exist.
    - Trying to finish an already finished match.

## Setup and Usage
Note: This project does not have a main class. The code is designed as a library, and the recommended way to verify its functionality is by running the tests.
### Prerequisites

- JDK 11 or higher (built with JDK 17)
- Gradle 8 (if you want to build from source)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/zebonair/scoreboard-app.git
   
2. Navigate to the project directory and run the tests:
   ```bash
      gradle test
