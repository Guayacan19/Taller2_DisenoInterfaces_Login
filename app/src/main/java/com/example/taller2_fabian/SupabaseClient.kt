package com.example.taller2_fabian

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client = createSupabaseClient (

        supabaseUrl = "https://oaqtoizfvrfbmtfrkydk.supabase.co",
        supabasekey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9hcXRvaXpmdnJmYm10ZnJreWRrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU5NDIwMzIsImV4cCI6MjA5MTUxODAzMn0.iKnYrTyJT5oYltRsFCkUD2SRgobd_1cjZoPX9QGDc9Y"
    )
    {
        install (Postgrest)
        install (Auth)
    }
}
