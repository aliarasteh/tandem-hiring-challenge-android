## **Community List Page**

This module is implemented to show community items in a list.



### Features

- **Community List**: 
  - Community items loaded from local database page by page using paging library
  - If no data is loaded or a new page requested (when scrolling list reaches the end) Makes network calls to [community API](https://tandem2019.web.app/api/community_1.json) and fetch data from the server.
  - Newly loaded community objects are saved in the local database (insert new objects and update existing ones)
  - Paging triggered to apply changes (show newly loaded objects or update changed ones)
- **Like Community**: every community default state is "not liked". User can toggle state by clicking on the like button. community state will be cached in local database and remains when relaunching the app.





Two types of fragments are implemented, one is  CommunityFragment which is the normal implementation of loading list of objects with Paging library, and the other one named CommunityFragmentSimple is a simple implementation using paging helper function and classes avoiding boilerplate codes.

When having multiple list pages these helper classes would really help us to speed up the development process with easy usage and less code. check classes and functions in the [paging directory](../../core/component/src/main/java/net/tandem/component/paging) under the component module.
