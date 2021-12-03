package nl.th7mo.spotify.token;

import nl.th7mo.connection.StatusCode;
import nl.th7mo.connection.BadRequestException;
import nl.th7mo.connection.InvalidCredentialsForTokenException;
import nl.th7mo.connection.InvalidRequestTokenPathException;

import java.io.IOException;
import java.net.HttpURLConnection;

public class SpotifyTokenDAOExceptionHandler {

    private static HttpURLConnection connection;

    public static void handleStatusCodes(HttpURLConnection connection)
            throws IOException, BadRequestException {
        SpotifyTokenDAOExceptionHandler.connection = connection;
        int responseCode = getResponseCode();

        if (responseCode != StatusCode.OK.codeNumber()) {
            handleBadRequestStatusCodes(responseCode);
        }
    }

    private static int getResponseCode() throws IOException {
        return connection.getResponseCode();
    }

    private static void handleBadRequestStatusCodes(int responseCode)
            throws InvalidCredentialsForTokenException, InvalidRequestTokenPathException {
        if (responseCode == StatusCode.BAD_REQUEST.codeNumber()) {
            throwInvalidCredentialsForTokenException();
        }

        throwInvalidRequestTokenPathException();
    }

    private static void throwInvalidCredentialsForTokenException()
            throws InvalidCredentialsForTokenException {
        throw new InvalidCredentialsForTokenException(
                "Bad Request, Credentials for token are invalid:" +
                "\n\nClient_Id: " +
                System.getProperty("SPOTIFY_CLIENT_ID") +
                "\nClient_Secret: " +
                System.getProperty("SPOTIFY_CLIENT_SECRET")
        );
    }

    private static void throwInvalidRequestTokenPathException()
            throws InvalidRequestTokenPathException {
        throw new InvalidRequestTokenPathException(
                "Bad Request, path to the token endpoint is wrong: " +
                "\nPath: " + System.getProperty("SPOTIFY_TOKEN_URL")
        );
    }
}
