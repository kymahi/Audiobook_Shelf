package com.kymahi.audiobookshelf.api

object APIPath {
    /**
     * This endpoint is a simple check to see if the server is operating and responding with JSON correctly.
     *
     * GET http://abs.example.com/ping
     */
    const val PING = "ping"

    /**
     * This endpoint logs in a client to the server, returning information about the user and server.
     *
     * POST http://abs.example.com/login
     *
     * Parameter	Type	Description
     * username	    String	The username to log in with.
     * password	    String	The password of the user.
     */
    const val LOGIN = "login"

    /**
     * This endpoint logs out a client from the server.
     *
     * POST http://abs.example.com/logout
     *
     * API Auth Token Required
     */
    const val LOGOUT = "logout"
}