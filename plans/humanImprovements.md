Refactoring Improvements

What improvements can be made to the overall design or architecture?
The overall structure of the code, reducing redundancies
Which large methods or classes can be split into smaller, more modular components?
Maybe a class such as the video processor and something like the binarygroup finder
Are there unused files, methods, or variables that should be removed?
Yes! For example there is an unused dependency in pom.xml
Where would additional interfaces (or abstractions) be appropriate?
Maybe for something like the group finders ?
How can the code be made simpler, more usable, and easier to maintain?
There are some commented out code that we can takeout to make things clearer
Any other refactoring opportunities?
definitely in the logic of our main src code. 

Testing Improvements

Which portions of the codebase are untested or only lightly tested?
Argument parsing logic?
Where are the highest-priority areas for adding new tests?
Video submission length, pathing, types of submissions
Are there specific edge cases not currently covered?
No test cases for very large videos
Any other testing-related improvements?

Error Handling Improvements

Which parts of the codebase are brittle or prone to breaking?
I think the pathing ? It might break depending on the os
Where should more specific exceptions be used instead of generic ones?
Wherever we have a broad exception we could swap to more specific exceptions

Where can input validation be added or strengthened?
File paths, Image and video format
How can error logging, surfacing, or resolution be improved?
Adding descriptive error messages and logging context 
Any additional error-handling improvements?

Documentation Improvements

Which classes or methods are missing Javadoc/JSdoc?
A lot or even all of our classes are missing javadoc, so we could add that 
Where could existing documentation be clarified or improved?
All of them could use more comments 
Are there sections of dead/commented-out code that should be removed?
Yes! As stated earlier there are some old code that was commented out 
What are the most important areas that need documentation for readability?
The video processing class, high level explanation of the processing pipeline?
Any other documentation improvements?

Performance Improvements (Optional)

What parts of the codebase, tests, or Docker setup run particularly slowly?

Which speed improvements would benefit development or runtime the most?

Any additional performance-related improvements?

Security Improvements (Optional)

Are any dependencies/packages out of date or flagged for vulnerabilities?

Where can input validation be improved to prevent misuse or injection attacks?

Any additional security-related improvements?

Bug Fixes (Optional)

What known bugs currently exist?

Which parts of the code might be causing them?

Any other bug-related improvements?