package webFrontCommonUtils;

public class InternalServerErrorException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Parameterless Constructor
    public InternalServerErrorException() {}

    // Constructor that accepts a message
    public InternalServerErrorException(String message)
    {
       super(message);
    }
}