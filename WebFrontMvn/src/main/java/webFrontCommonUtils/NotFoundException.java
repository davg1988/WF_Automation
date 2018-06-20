package webFrontCommonUtils;

public class NotFoundException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Parameterless Constructor
    public NotFoundException() {}

    // Constructor that accepts a message
    public NotFoundException(String message)
    {
       super(message);
    }
}