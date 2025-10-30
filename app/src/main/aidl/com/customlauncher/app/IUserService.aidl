package com.customlauncher.app;

interface IUserService {
    void destroy() = 16777114; // Destroy method defined by Shizuku server
    
    void exit() = 1; // Exit method
    
    String executeCommand(String cmd) = 2;
}
