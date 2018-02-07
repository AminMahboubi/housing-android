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

/**
 * Created by amin on 2/6/18.
 */

@RunWith(MockitoJUnitRunner.class)
public class FavouriteTest extends TestCase {

    @Mock
    Context context;

    @Mock
    SharedPreferences sharedPreferences;

    @Mock
    SharedPreferences.Editor editor;

    Favourite favourite;

    @Before
    public void initMocks() {
        Mockito.when(context.getSharedPreferences(Mockito.anyString(), Mockito.anyInt())).thenReturn(sharedPreferences);
        Mockito.when(sharedPreferences.edit()).thenReturn(editor);
        Mockito.when(editor.putBoolean(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(editor);
        Mockito.when(sharedPreferences.getBoolean(Mockito.eq("2"), Mockito.anyBoolean())).thenReturn(true);

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
        Mockito.verify(editor).apply();
    }

    @Test
    public void getNullFavTest() {
        assertFalse(favourite.getFav("1"));
    }

    @Test
    public void getFavTest() {
        assertTrue(favourite.getFav("2"));
    }
}