package com.aminmahboubi.housing.model;

import android.content.Context;
import android.content.SharedPreferences;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Created by amin on 2/6/18.
 */

@RunWith(MockitoJUnitRunner.class)
public class FavouriteTest extends TestCase {

    @Mock
    Context context;

    Favourite favourite;

    @Mock
    SharedPreferences sharedPreferences;
    @Mock
    SharedPreferences.Editor editor;


    @Before
    public void initMocks() {

        Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        Mockito.when(sharedPreferences.edit()).thenReturn(editor);
        Mockito.when(editor.putString(anyString(), anyString())).thenReturn(editor);



        favourite = Favourite.getInstance(context);


    }

    @Test
    public void instanceTest() {
        assertNotNull(favourite);
        assertEquals(favourite, Favourite.getInstance(context));
    }

    @Test
    public void flipFavTest() {
        assertTrue(favourite.flipFav("1"));
        assertTrue(favourite.getFav("1"));

        assertFalse(favourite.flipFav("1"));
        assertFalse(favourite.getFav("1"));

    }
}
