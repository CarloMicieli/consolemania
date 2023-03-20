# The REST API

## The Catalog Api

|        Endpoint	         |  Method  | Req. body  | Status |  Resp. body  | Description    		                                   |
|:------------------------:|:--------:|:----------:|:------:|:------------:|:------------------------------------------------------|
|        `/games`          |  `POST`  |   `Game`   |  201   |              | Add a new game to the catalog                         |
|      `/games/{urn}`      |  `PUT`   |   `Game`   |  204   |              | Update the game with the given `{urn}`                |
|      `/games/{urn}`      |  `GET`   |            |  200   |    `Game`    | Get the game with the given `{urn}`                   |
|      `/games/{urn}`      | `DELETE` |            |  204   |              | Delete the game with the given `{urn}`                |
|      `/platforms`        |  `POST`  | `Platform` |  201   |              | Add a new platform to the catalog                     |
|    `/platforms/{urn}`    |  `PUT`   | `Platform` |  204   |              | Update the platform with the given `{urn}`            |
|      `/platforms`        |  `GET`   |            |  200   | `Platform[]` | Get all platforms in the catalog                      |
|    `/platforms/{urn}`    |  `GET`   |            |  200   |  `Platform`  | Get the platform with the given `{urn}`               |
| `/platforms/{urn}/games` |  `GET`   |            |  200   |   `Game[]`   | Get all games for the platform with the given `{urn}` |
