package com.github.rizar.labchecker.test;

import java.util.*;

/**
 *
 * @author Rizar
 */
public class Log implements Iterable<Log.TypedMessage>
{
    public enum MessageType
    {
        INIT, INFO, ERROR_INFO, OK_INFO, ERROR, OK;
    }

    public class TypedMessage
    {
        private String message;
        private MessageType type;

        public String getMessage() {
			return message;
		}

		public MessageType getType() {
			return type;
		}

		public TypedMessage(String message, MessageType type)
        {
            this.message = message;
            this.type = type;
        }
    }

    private List<TypedMessage> messages = new ArrayList<TypedMessage> ();

    /*public void clear()
    {
        messages.clear();
    }*/

    public String getMessage(int index)
    {
        return messages.get(index).message;
    }

    public MessageType getType(int index)
    {
        return messages.get(index).type;
    }

    public void addMessage(MessageType type, String formatString, Object... args)
    {
        messages.add(new TypedMessage(String.format(formatString, args), type));
    }

    private Formatter currentFormatter;
    private MessageType currentType;

    public void startMessage(MessageType type)
    {
        currentFormatter = new Formatter ();
        currentType = type;
    }

    public void startMessage(MessageType type, String formatString, Object... args)
    {
        startMessage(type);
        appendToMessage(formatString, args);
    }

    public void appendToMessage(String formatString, Object... args)
    {
        currentFormatter.format(formatString, args);
    }

    public void finishMessage()
    {
        messages.add(new TypedMessage(currentFormatter.toString(), currentType));
    }

    public void finishMessage(String formatString, Object... args)
    {
        appendToMessage(formatString, args);
        finishMessage();
    }

    public void addAllMessages(Log otherLog)
    {
    	if (otherLog != null)
    		messages.addAll(otherLog.messages);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder ();
        for (TypedMessage tm : messages)
        {
            sb.append(tm.message);
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public int getLength()
    {
    	return messages.size();
    }
    
	@Override
	public Iterator<TypedMessage> iterator() {
    	return messages.iterator();
	}
}
